package com.directmefilm.service;

import com.directmefilm.dto.AnswerRequest;
import com.directmefilm.dto.MatchRequest;
import com.directmefilm.dto.MatchResponse;
import com.directmefilm.dto.MovieResponse;
import com.directmefilm.exception.InvalidQuestionnaireException;
import com.directmefilm.exception.ResourceNotFoundException;
import com.directmefilm.model.Director;
import com.directmefilm.model.MatchResult;
import com.directmefilm.model.Movie;
import com.directmefilm.model.Question;
import com.directmefilm.model.TraitAxis;
import com.directmefilm.repository.DirectorRepository;
import com.directmefilm.repository.MatchResultRepository;
import com.directmefilm.repository.MovieRepository;
import com.directmefilm.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MatchingService {

    private static final double QUESTION_WEIGHT = 0.82;
    private static final double FAVORITES_WEIGHT = 0.18;

    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;
    private final QuestionRepository questionRepository;
    private final MatchResultRepository matchResultRepository;
    private final CatalogService catalogService;

    public MatchingService(
            DirectorRepository directorRepository,
            MovieRepository movieRepository,
            QuestionRepository questionRepository,
            MatchResultRepository matchResultRepository,
            CatalogService catalogService
    ) {
        this.directorRepository = directorRepository;
        this.movieRepository = movieRepository;
        this.questionRepository = questionRepository;
        this.matchResultRepository = matchResultRepository;
        this.catalogService = catalogService;
    }

    @Transactional
    public MatchResponse createMatch(MatchRequest request) {
        List<Question> questions = questionRepository.findAllByOrderByDisplayOrderAsc();
        Map<Long, Integer> answers = validateAndIndexAnswers(request.answers(), questions);
        List<Movie> favorites = validateFavoriteMovies(request.favoriteMovieIds());
        List<Director> directors = directorRepository.findAllByOrderByNameAsc();

        Candidate winner = directors.stream()
                .map(director -> score(director, questions, answers, favorites))
                .sorted(Comparator.comparingDouble(Candidate::score).reversed()
                        .thenComparing(candidate -> candidate.director().getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No directors are configured."));

        List<String> reasons = buildReasons(winner.director(), questions, answers, favorites);
        MatchResult saved = matchResultRepository.save(new MatchResult(
                winner.director(),
                (int) Math.round(winner.score()),
                reasons,
                favorites.stream().map(Movie::getId).toList()
        ));

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MatchResponse getMatch(UUID id) {
        MatchResult match = matchResultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match " + id + " was not found."));
        return toResponse(match);
    }

    private Map<Long, Integer> validateAndIndexAnswers(
            List<AnswerRequest> submittedAnswers,
            List<Question> questions
    ) {
        Map<Long, Integer> answers = new HashMap<>();
        for (AnswerRequest answer : submittedAnswers) {
            if (answers.put(answer.questionId(), answer.value()) != null) {
                throw new InvalidQuestionnaireException(
                        "Each question can only be answered once."
                );
            }
        }

        Set<Long> expectedIds = questions.stream()
                .map(Question::getId)
                .collect(Collectors.toSet());
        if (!answers.keySet().equals(expectedIds)) {
            throw new InvalidQuestionnaireException(
                    "Please answer every current questionnaire question."
            );
        }
        return answers;
    }

    private List<Movie> validateFavoriteMovies(Set<Long> movieIds) {
        List<Movie> movies = movieRepository.findByIdIn(movieIds);
        if (movies.size() != movieIds.size()) {
            throw new InvalidQuestionnaireException(
                    "One or more selected favourite movies do not exist."
            );
        }
        return movies;
    }

    private Candidate score(
            Director director,
            List<Question> questions,
            Map<Long, Integer> answers,
            List<Movie> favorites
    ) {
        double earned = 0;
        double available = 0;

        for (Question question : questions) {
            int answer = answers.get(question.getId());
            int directorProfile = director.scoreFor(question.getAxis());
            double similarity = 1.0 - (Math.abs(answer - directorProfile) / 4.0);
            earned += similarity * question.getWeight();
            available += question.getWeight();
        }

        double questionnaireScore = earned / available * 100;
        long matchingFavorites = favorites.stream()
                .filter(movie -> movie.getDirector().getId().equals(director.getId()))
                .count();
        double favoriteScore = matchingFavorites * 100.0 / favorites.size();
        double total = questionnaireScore * QUESTION_WEIGHT + favoriteScore * FAVORITES_WEIGHT;
        return new Candidate(director, total);
    }

    private List<String> buildReasons(
            Director director,
            List<Question> questions,
            Map<Long, Integer> answers,
            List<Movie> favorites
    ) {
        List<String> reasons = new ArrayList<>();
        List<String> matchingTitles = favorites.stream()
                .filter(movie -> movie.getDirector().getId().equals(director.getId()))
                .map(Movie::getTitle)
                .sorted()
                .toList();

        if (!matchingTitles.isEmpty()) {
            reasons.add("Your favourites include " + String.join(" and ", matchingTitles)
                    + ", a strong signal for this filmmaker's voice.");
        }

        Map<TraitAxis, Double> agreementByAxis = new EnumMap<>(TraitAxis.class);
        for (TraitAxis axis : TraitAxis.values()) {
            double agreement = questions.stream()
                    .filter(question -> question.getAxis() == axis)
                    .mapToDouble(question ->
                            1.0 - Math.abs(
                                    answers.get(question.getId()) - director.scoreFor(axis)
                            ) / 4.0
                    )
                    .average()
                    .orElse(0);
            agreementByAxis.put(axis, agreement);
        }

        agreementByAxis.entrySet().stream()
                .sorted(Map.Entry.<TraitAxis, Double>comparingByValue().reversed()
                        .thenComparing(entry -> entry.getKey().name()))
                .map(entry -> reasonFor(entry.getKey(), director.scoreFor(entry.getKey())))
                .filter(reason -> !reasons.contains(reason))
                .limit(3 - reasons.size())
                .forEach(reasons::add);

        return reasons;
    }

    private String reasonFor(TraitAxis axis, int profile) {
        return switch (axis) {
            case EMOTION -> profile > 0
                    ? "You lean toward emotionally candid, character-led stories."
                    : "You appreciate ideas and restraint more than overt sentiment.";
            case VISUAL_STYLE -> profile > 0
                    ? "You respond to deliberate composition and a strong visual atmosphere."
                    : "You prefer the filmmaking to stay natural and close to the characters.";
            case COMPLEXITY -> profile > 0
                    ? "You enjoy layered stories that reward active attention."
                    : "You value a clear emotional through-line over a narrative puzzle.";
            case DARKNESS -> profile > 0
                    ? "You are comfortable with tension, ambiguity, and morally difficult territory."
                    : "You gravitate toward hopeful energy and a generous view of people.";
            case EXPERIMENTAL -> profile > 0
                    ? "You like filmmakers who bend genre and take surprising creative swings."
                    : "You favour confident storytelling within a recognizable form.";
        };
    }

    private MatchResponse toResponse(MatchResult match) {
        List<Movie> directorMovies = movieRepository
                .findByDirectorIdOrderByReleaseYearDesc(match.getDirector().getId());
        Set<Long> favoriteIds = Set.copyOf(match.getFavoriteMovieIds());

        List<MovieResponse> recommendations = Stream.concat(
                        directorMovies.stream().filter(movie -> !favoriteIds.contains(movie.getId())),
                        directorMovies.stream().filter(movie -> favoriteIds.contains(movie.getId()))
                )
                .map(CatalogService::toMovieResponse)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new LinkedHashSet<MovieResponse>()),
                        set -> set.stream().limit(3).toList()
                ));

        return new MatchResponse(
                match.getId(),
                catalogService.toDirectorResponse(match.getDirector()),
                match.getMatchScore(),
                match.getReasons(),
                recommendations,
                match.getCreatedAt()
        );
    }

    private record Candidate(Director director, double score) {
    }
}

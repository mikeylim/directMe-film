package com.directmefilm.service;

import com.directmefilm.dto.AnswerRequest;
import com.directmefilm.dto.MatchRequest;
import com.directmefilm.dto.MatchResponse;
import com.directmefilm.exception.InvalidQuestionnaireException;
import com.directmefilm.model.Director;
import com.directmefilm.model.Movie;
import com.directmefilm.model.Question;
import com.directmefilm.repository.DirectorRepository;
import com.directmefilm.repository.MovieRepository;
import com.directmefilm.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MatchingServiceTests {

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CommandLineRunner seedCatalog;

    @Test
    void identicalAnswersProduceTheSameDeterministicResult() {
        MatchRequest request = requestMatching("christopher-nolan");

        MatchResponse first = matchingService.createMatch(request);
        MatchResponse second = matchingService.createMatch(request);

        assertThat(first.matchedDirector().slug()).isEqualTo("christopher-nolan");
        assertThat(second.matchedDirector().slug()).isEqualTo(first.matchedDirector().slug());
        assertThat(second.matchScore()).isEqualTo(first.matchScore());
        assertThat(second.reasons()).isEqualTo(first.reasons());
    }

    @Test
    void createdMatchCanBeRetrievedByItsId() {
        MatchResponse created = matchingService.createMatch(requestMatching("greta-gerwig"));

        MatchResponse retrieved = matchingService.getMatch(created.id());

        assertThat(retrieved).isEqualTo(created);
        assertThat(retrieved.recommendedMovies()).hasSize(3);
        assertThat(retrieved.reasons()).hasSizeBetween(2, 3);
    }

    @Test
    void incompleteQuestionnaireIsRejected() {
        MatchRequest complete = requestMatching("denis-villeneuve");
        MatchRequest incomplete = new MatchRequest(
                complete.answers().subList(0, complete.answers().size() - 1),
                complete.favoriteMovieIds()
        );

        assertThatThrownBy(() -> matchingService.createMatch(incomplete))
                .isInstanceOf(InvalidQuestionnaireException.class)
                .hasMessageContaining("answer every");
    }

    @Test
    void expandedCatalogContainsThirtyDirectorsAndNinetySevenFilms() {
        assertThat(directorRepository.count()).isEqualTo(30);
        assertThat(movieRepository.count()).isEqualTo(97);
        assertThat(directorRepository.findAll()).allSatisfy(director ->
                assertThat(movieRepository.findByDirectorIdOrderByReleaseYearDesc(
                        director.getId()
                )).hasSizeGreaterThanOrEqualTo(3)
        );
    }

    @Test
    void runningCatalogSeedAgainDoesNotCreateDuplicates() throws Exception {
        seedCatalog.run();

        assertThat(directorRepository.count()).isEqualTo(30);
        assertThat(movieRepository.count()).isEqualTo(97);
    }

    private MatchRequest requestMatching(String directorSlug) {
        Director director = directorRepository.findBySlug(directorSlug).orElseThrow();
        List<Question> questions = questionRepository.findAllByOrderByDisplayOrderAsc();
        List<AnswerRequest> answers = questions.stream()
                .map(question -> new AnswerRequest(
                        question.getId(),
                        director.scoreFor(question.getAxis())
                ))
                .toList();

        Set<Long> favoriteIds = movieRepository
                .findByDirectorIdOrderByReleaseYearDesc(director.getId())
                .stream()
                .limit(2)
                .map(Movie::getId)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        return new MatchRequest(answers, favoriteIds);
    }
}

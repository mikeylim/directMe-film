package com.directmefilm.service;

import com.directmefilm.dto.DirectorResponse;
import com.directmefilm.dto.MovieResponse;
import com.directmefilm.dto.QuestionResponse;
import com.directmefilm.model.Director;
import com.directmefilm.model.Movie;
import com.directmefilm.repository.DirectorRepository;
import com.directmefilm.repository.MovieRepository;
import com.directmefilm.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogService {

    private final QuestionRepository questionRepository;
    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;

    public CatalogService(
            QuestionRepository questionRepository,
            DirectorRepository directorRepository,
            MovieRepository movieRepository
    ) {
        this.questionRepository = questionRepository;
        this.directorRepository = directorRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestions() {
        return questionRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(question -> new QuestionResponse(
                        question.getId(),
                        question.getDisplayOrder(),
                        question.getPrompt(),
                        question.getLowLabel(),
                        question.getHighLabel()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DirectorResponse> getDirectors() {
        return directorRepository.findAllByOrderByNameAsc().stream()
                .map(this::toDirectorResponse)
                .toList();
    }

    DirectorResponse toDirectorResponse(Director director) {
        List<MovieResponse> movies = movieRepository
                .findByDirectorIdOrderByReleaseYearDesc(director.getId())
                .stream()
                .map(CatalogService::toMovieResponse)
                .toList();

        return new DirectorResponse(
                director.getId(),
                director.getSlug(),
                director.getName(),
                director.getDescription(),
                director.getSignatureStyle(),
                movies
        );
    }

    static MovieResponse toMovieResponse(Movie movie) {
        return new MovieResponse(movie.getId(), movie.getTitle(), movie.getReleaseYear());
    }
}

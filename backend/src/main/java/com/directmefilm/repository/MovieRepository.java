package com.directmefilm.repository;

import com.directmefilm.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitleAndDirectorId(String title, Long directorId);

    List<Movie> findAllByOrderByTitleAsc();

    List<Movie> findByDirectorIdOrderByReleaseYearDesc(Long directorId);

    List<Movie> findByIdIn(Collection<Long> ids);
}

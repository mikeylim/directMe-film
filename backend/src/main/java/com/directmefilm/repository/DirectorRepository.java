package com.directmefilm.repository;

import com.directmefilm.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    List<Director> findAllByOrderByNameAsc();

    Optional<Director> findBySlug(String slug);
}

package com.directmefilm.repository;

import com.directmefilm.model.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchResultRepository extends JpaRepository<MatchResult, UUID> {
}

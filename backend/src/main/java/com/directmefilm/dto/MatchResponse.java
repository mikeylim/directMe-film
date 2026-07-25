package com.directmefilm.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MatchResponse(
        UUID id,
        DirectorResponse matchedDirector,
        int matchScore,
        List<String> reasons,
        List<MovieResponse> recommendedMovies,
        Instant createdAt
) {
}

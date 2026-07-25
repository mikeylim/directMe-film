package com.directmefilm.dto;

public record MovieResponse(
        Long id,
        String title,
        int releaseYear
) {
}

package com.directmefilm.dto;

import java.util.List;

public record DirectorResponse(
        Long id,
        String slug,
        String name,
        String description,
        String signatureStyle,
        List<MovieResponse> movies
) {
}

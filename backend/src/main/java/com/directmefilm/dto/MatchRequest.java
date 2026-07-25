package com.directmefilm.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record MatchRequest(
        @NotEmpty
        @Size(min = 8, max = 10)
        List<@Valid AnswerRequest> answers,

        @NotEmpty
        @Size(min = 2, max = 5)
        Set<@Positive Long> favoriteMovieIds
) {
}

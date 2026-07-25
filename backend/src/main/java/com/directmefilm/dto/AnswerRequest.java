package com.directmefilm.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AnswerRequest(
        @NotNull @Positive Long questionId,
        @NotNull @Min(-2) @Max(2) Integer value
) {
}

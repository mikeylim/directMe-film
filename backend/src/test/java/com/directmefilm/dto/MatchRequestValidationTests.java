package com.directmefilm.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MatchRequestValidationTests {

    private final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Test
    void rejectsOutOfRangeAnswersAndTooFewFavorites() {
        List<AnswerRequest> answers = java.util.stream.IntStream.rangeClosed(1, 8)
                .mapToObj(id -> new AnswerRequest((long) id, id == 1 ? 3 : 0))
                .toList();
        MatchRequest request = new MatchRequest(answers, Set.of(1L));

        Set<String> paths = validator.validate(request).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(java.util.stream.Collectors.toSet());

        assertThat(paths).contains("answers[0].value", "favoriteMovieIds");
    }
}

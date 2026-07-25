package com.directmefilm.dto;

public record QuestionResponse(
        Long id,
        int position,
        String prompt,
        String lowLabel,
        String highLabel
) {
}

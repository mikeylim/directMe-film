package com.directmefilm.controller;

import com.directmefilm.dto.QuestionResponse;
import com.directmefilm.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final CatalogService catalogService;

    public QuestionController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<QuestionResponse> getQuestions() {
        return catalogService.getQuestions();
    }
}

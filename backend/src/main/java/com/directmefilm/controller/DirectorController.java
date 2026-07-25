package com.directmefilm.controller;

import com.directmefilm.dto.DirectorResponse;
import com.directmefilm.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {

    private final CatalogService catalogService;

    public DirectorController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<DirectorResponse> getDirectors() {
        return catalogService.getDirectors();
    }
}

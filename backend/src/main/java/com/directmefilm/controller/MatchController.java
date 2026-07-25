package com.directmefilm.controller;

import com.directmefilm.dto.MatchRequest;
import com.directmefilm.dto.MatchResponse;
import com.directmefilm.service.MatchingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchingService matchingService;

    public MatchController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping
    public ResponseEntity<MatchResponse> createMatch(
            @Valid @RequestBody MatchRequest request
    ) {
        MatchResponse response = matchingService.createMatch(request);
        return ResponseEntity
                .created(URI.create("/api/matches/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public MatchResponse getMatch(@PathVariable UUID id) {
        return matchingService.getMatch(id);
    }
}

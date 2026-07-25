package com.directmefilm.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "match_results")
public class MatchResult {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @Column(nullable = false)
    private int matchScore;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "match_reasons", joinColumns = @JoinColumn(name = "match_id"))
    @OrderColumn(name = "reason_order")
    @Column(name = "reason", nullable = false, length = 400)
    private List<String> reasons = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "match_favorite_movies", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "movie_id", nullable = false)
    private List<Long> favoriteMovieIds = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected MatchResult() {
    }

    public MatchResult(
            Director director,
            int matchScore,
            List<String> reasons,
            List<Long> favoriteMovieIds
    ) {
        this.director = director;
        this.matchScore = matchScore;
        this.reasons = new ArrayList<>(reasons);
        this.favoriteMovieIds = new ArrayList<>(favoriteMovieIds);
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Director getDirector() {
        return director;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public List<String> getReasons() {
        return List.copyOf(reasons);
    }

    public List<Long> getFavoriteMovieIds() {
        return List.copyOf(favoriteMovieIds);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

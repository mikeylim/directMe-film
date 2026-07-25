package com.directmefilm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @jakarta.persistence.Column(nullable = false)
    private String title;

    @jakarta.persistence.Column(nullable = false)
    private int releaseYear;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    protected Movie() {
    }

    public Movie(String title, int releaseYear, Director director) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.director = director;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public Director getDirector() {
        return director;
    }
}

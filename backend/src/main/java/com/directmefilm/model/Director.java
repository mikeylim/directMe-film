package com.directmefilm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "directors")
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 600)
    private String description;

    @Column(nullable = false)
    private String signatureStyle;

    @Column(nullable = false)
    private int emotion;

    @Column(nullable = false)
    private int visualStyle;

    @Column(nullable = false)
    private int complexity;

    @Column(nullable = false)
    private int darkness;

    @Column(nullable = false)
    private int experimental;

    protected Director() {
    }

    public Director(
            String slug,
            String name,
            String description,
            String signatureStyle,
            int emotion,
            int visualStyle,
            int complexity,
            int darkness,
            int experimental
    ) {
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.signatureStyle = signatureStyle;
        this.emotion = emotion;
        this.visualStyle = visualStyle;
        this.complexity = complexity;
        this.darkness = darkness;
        this.experimental = experimental;
    }

    public int scoreFor(TraitAxis axis) {
        return switch (axis) {
            case EMOTION -> emotion;
            case VISUAL_STYLE -> visualStyle;
            case COMPLEXITY -> complexity;
            case DARKNESS -> darkness;
            case EXPERIMENTAL -> experimental;
        };
    }

    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSignatureStyle() {
        return signatureStyle;
    }
}

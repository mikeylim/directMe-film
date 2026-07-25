package com.directmefilm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String prompt;

    @Column(nullable = false)
    private String lowLabel;

    @Column(nullable = false)
    private String highLabel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TraitAxis axis;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false, unique = true)
    private int displayOrder;

    protected Question() {
    }

    public Question(
            String prompt,
            String lowLabel,
            String highLabel,
            TraitAxis axis,
            double weight,
            int displayOrder
    ) {
        this.prompt = prompt;
        this.lowLabel = lowLabel;
        this.highLabel = highLabel;
        this.axis = axis;
        this.weight = weight;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getLowLabel() {
        return lowLabel;
    }

    public String getHighLabel() {
        return highLabel;
    }

    public TraitAxis getAxis() {
        return axis;
    }

    public double getWeight() {
        return weight;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}

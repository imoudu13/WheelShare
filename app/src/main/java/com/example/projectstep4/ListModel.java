package com.example.projectstep4;

public class ListModel {
    private String name;
    private float starRating;

    public ListModel(String name, float starRating) {
        this.name = name;
        this.starRating = starRating;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getStarRating() {
        return starRating;
    }

    public void setStarRating(float starRating) {
        this.starRating = starRating;
    }
}



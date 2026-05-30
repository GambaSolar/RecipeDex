package com.example.recipespringandroid.api.dto;

import com.example.recipespringandroid.models.Recipe;

public class RecipeDetailResponse {

    private Recipe recipe;
    private double averageRating;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
package com.example.recipespringandroid.api.repository;

import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.api.dto.RecipeDetailResponse;
import com.example.recipespringandroid.models.Recipe;

import java.util.List;

import retrofit2.Call;

public class RecipeRepository {

    private final ApiService apiService;

    public RecipeRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<List<Recipe>> getRecipes() {
        return apiService.getAllRecipes();
    }

    public Call<Recipe> createRecipe(int userId, Recipe recipe) {
        return apiService.createRecipe(userId, recipe);
    }

    public Call<RecipeDetailResponse> getRecipeById(int id) {
        return apiService.getRecipeById(id);
    }
}
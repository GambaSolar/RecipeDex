package com.example.recipespringandroid.api.repository;

import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.api.dto.ReviewDTO;
import com.example.recipespringandroid.models.Review;

import java.util.List;

import retrofit2.Call;

public class ReviewRepository {

    private final ApiService apiService;

    public ReviewRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<Review> addReview(int userId, int recipeId, Review review) {
        return apiService.addReview(userId, recipeId, review);
    }

    public Call<List<ReviewDTO>> getReviewsByRecipe(int recipeId) {
        return apiService.getReviewsByRecipe(recipeId);
    }
}
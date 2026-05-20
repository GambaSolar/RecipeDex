package com.example.recipespringandroid.api.repository;

import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.models.Ingredient;
import java.util.List;
import retrofit2.Call;

public class IngredientRepository {

    private final ApiService apiService;

    public IngredientRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<List<Ingredient>> getIngredients() {
        return apiService.getIngredients();
    }
}
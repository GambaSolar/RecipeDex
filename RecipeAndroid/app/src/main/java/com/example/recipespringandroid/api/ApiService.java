package com.example.recipespringandroid.api;

import com.example.recipespringandroid.api.dto.LoginRequest;
import com.example.recipespringandroid.api.dto.RegisterRequest;
import com.example.recipespringandroid.models.Ingredient;
import com.example.recipespringandroid.models.Recipe;
import com.example.recipespringandroid.models.Review;
import com.example.recipespringandroid.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("users/register")
    Call<User> register(
            @Body RegisterRequest request
    );

    @POST("users/login")
    Call<User> login(
            @Body LoginRequest request
    );

    @GET("users")
    Call<List<User>> getAllUsers();

    @GET("users/{id}")
    Call<User> getUserById(
            @Path("id") int id
    );

    @GET("recipes")
    Call<List<Recipe>> getAllRecipes();

    @GET("recipes/{id}")
    Call<Recipe> getRecipeById(
            @Path("id") int id
    );

    @GET("recipes/user/{userId}")
    Call<List<Recipe>> getRecipesByUser(
            @Path("userId") int userId
    );

    @GET("recipes/search")
    Call<List<Recipe>> searchRecipes(
            @Query("name") String name
    );

    @POST("recipes")
    Call<Recipe> createRecipe(
            @Query("userId") int userId,
            @Body Recipe recipe
    );

    @DELETE("recipes/{id}")
    Call<Void> deleteRecipe(
            @Path("id") int id
    );

    @GET("ingredients")
    Call<List<Ingredient>> getIngredients();

    @POST("ingredients")
    Call<Ingredient> createIngredient(
            @Body Ingredient ingredient
    );

    @POST("recipe-ingredients")
    Call<Void> addRecipeIngredient(
            @Query("recipeId") Integer recipeId,
            @Query("ingredientId") Integer ingredientId
    );

    @GET("recipe-ingredients/ingredient/{ingredientId}")
    Call<List<Recipe>> getRecipesByIngredient(
            @Path("ingredientId") int ingredientId
    );

    @POST("reviews")
    Call<Review> addReview(
            @Query("userId") int userId,
            @Query("recipeId") int recipeId,
            @Body Review review
    );

    @GET("reviews/recipe/{recipeId}")
    Call<List<Review>> getReviewsByRecipe(
            @Path("recipeId") int recipeId
    );

    @GET("favorites/user/{userId}")
    Call<List<Recipe>> getFavorites(
            @Path("userId") int userId
    );

    @POST("favorites")
    Call<Void> addFavorite(
            @Query("userId") int userId,
            @Query("recipeId") int recipeId
    );

    @DELETE("favorites")
    Call<Void> removeFavorite(
            @Query("userId") int userId,
            @Query("recipeId") int recipeId
    );

    @GET("favorites/count/{recipeId}")
    Call<Integer> getFavoriteCount(
            @Path("recipeId") int recipeId
    );
}
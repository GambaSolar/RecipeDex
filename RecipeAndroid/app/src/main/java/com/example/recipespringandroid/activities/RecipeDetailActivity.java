package com.example.recipespringandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.api.dto.RecipeDetailResponse;
import com.example.recipespringandroid.models.Recipe;
import com.example.recipespringandroid.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvTime, tvFavoriteCount;
    private Button btnToggleFavorite, btnComments;

    private SessionManager sessionManager;
    private ApiService api;

    private int recipeId;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvTime = findViewById(R.id.tvTime);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);

        btnToggleFavorite = findViewById(R.id.btnToggleFavorite);
        btnComments = findViewById(R.id.btnComments);

        sessionManager = new SessionManager(this);
        api = ApiClient.getClient().create(ApiService.class);

        recipeId = getIntent().getIntExtra("recipeId", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Receta inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRecipe(recipeId);
        loadFavoriteCount();
        checkIfFavorite();

        btnToggleFavorite.setOnClickListener(v -> toggleFavorite());

        btnComments.setOnClickListener(v -> {
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ReviewsActivity.class);
            intent.putExtra("recipeId", recipeId);
            startActivity(intent);
        });
    }

    private void loadRecipe(int id) {

        api.getRecipeById(id).enqueue(new Callback<RecipeDetailResponse>() {

            @Override
            public void onResponse(Call<RecipeDetailResponse> call,
                                   Response<RecipeDetailResponse> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(RecipeDetailActivity.this,
                            "Error cargando receta",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                Recipe recipe = response.body().getRecipe();

                if (recipe == null) {
                    Toast.makeText(RecipeDetailActivity.this,
                            "Receta inválida",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                tvName.setText(recipe.getName());
                tvDescription.setText(recipe.getDescription());

                int time = recipe.getPreparationTime() != null
                        ? recipe.getPreparationTime()
                        : 0;

                tvTime.setText("Tiempo de preparación: " + time + " min");
            }

            @Override
            public void onFailure(Call<RecipeDetailResponse> call, Throwable t) {
                Log.e("RECIPE_ERROR", t.getMessage(), t);
                Toast.makeText(RecipeDetailActivity.this,
                        "Error de red",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFavoriteCount() {

        api.getFavoriteCount(recipeId).enqueue(new Callback<Integer>() {

            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                int count = (response.isSuccessful() && response.body() != null)
                        ? response.body()
                        : 0;

                tvFavoriteCount.setText("⭐ " + count + " favoritos");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("FAV_COUNT", t.getMessage(), t);
                tvFavoriteCount.setText("⭐ 0 favoritos");
            }
        });
    }

    private void checkIfFavorite() {

        if (!sessionManager.isLoggedIn()) {
            isFavorite = false;
            updateFavoriteButton();
            return;
        }

        int userId = sessionManager.getUserId();

        api.isFavorite(userId, recipeId).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                isFavorite = response.isSuccessful()
                        && Boolean.TRUE.equals(response.body());

                updateFavoriteButton();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("FAV_CHECK", t.getMessage(), t);
                isFavorite = false;
                updateFavoriteButton();
            }
        });
    }

    private void toggleFavorite() {

        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();

        if (!isFavorite) {

            api.addFavorite(userId, recipeId).enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.isSuccessful()) {
                        isFavorite = true;
                        updateFavoriteButton();
                        loadFavoriteCount();
                    } else {
                        Toast.makeText(RecipeDetailActivity.this,
                                "No se pudo añadir a favoritos",
                                Toast.LENGTH_SHORT).show();
                        checkIfFavorite();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("FAV_ADD", t.getMessage(), t);
                    Toast.makeText(RecipeDetailActivity.this,
                            "Error de red al añadir",
                            Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            api.removeFavorite(userId, recipeId).enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    Log.e("FAV_DELETE",
                            "CODE=" + response.code() +
                                    " URL=" + call.request().url());

                    if (response.isSuccessful()) {
                        isFavorite = false;
                        updateFavoriteButton();
                        loadFavoriteCount();
                    } else {
                        Toast.makeText(RecipeDetailActivity.this,
                                "No se pudo eliminar de favoritos",
                                Toast.LENGTH_SHORT).show();

                        checkIfFavorite();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("FAV_REMOVE", t.getMessage(), t);
                    Toast.makeText(RecipeDetailActivity.this,
                            "Error de red al eliminar",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateFavoriteButton() {
        btnToggleFavorite.setText(
                isFavorite ? "Quitar de favoritos" : "Añadir a favoritos"
        );
    }
}
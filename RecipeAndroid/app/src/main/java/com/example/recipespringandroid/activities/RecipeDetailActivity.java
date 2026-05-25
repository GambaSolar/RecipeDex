package com.example.recipespringandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.models.Recipe;
import com.example.recipespringandroid.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvDescription;
    private TextView tvTime;
    private TextView tvFavoriteCount;

    private Button btnToggleFavorite;
    private Button btnComments;

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

        btnToggleFavorite.setOnClickListener(v -> toggleFavorite());

        btnComments.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewsActivity.class);
            intent.putExtra("recipeId", recipeId);
            startActivity(intent);
        });
    }

    private void loadRecipe(int id) {
        api.getRecipeById(id).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Recipe recipe = response.body();

                    tvName.setText(recipe.getName());
                    tvDescription.setText(recipe.getDescription());
                    Integer time = recipe.getPreparationTime();

                    tvTime.setText(
                            "Tiempo: " +
                                    (time != null ? time : 0) +
                                    " min"
                    );
                } else {
                    Toast.makeText(RecipeDetailActivity.this,
                            "Error cargando receta",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(RecipeDetailActivity.this,
                        "Error de red",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadFavoriteCount() {
        api.getFavoriteCount(recipeId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvFavoriteCount.setText("⭐ " + response.body() + " favoritos");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {}
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
                        btnToggleFavorite.setText("Eliminar favorito");
                        loadFavoriteCount();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {}
            });

        } else {

            api.removeFavorite(userId, recipeId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        isFavorite = false;
                        btnToggleFavorite.setText("Añadir a favoritos");
                        loadFavoriteCount();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {}
            });
        }
    }
}
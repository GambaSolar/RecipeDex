package com.example.recipespringandroid.activities;

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

    private TextView tvName, tvDescription, tvTime;
    private Button btnFavorite;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvTime = findViewById(R.id.tvTime);
        btnFavorite = findViewById(R.id.btnFavorite);

        sessionManager = new SessionManager(this);

        int recipeId = getIntent().getIntExtra("recipeId", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Error: receta inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadRecipe(recipeId);

        btnFavorite.setOnClickListener(v -> addFavorite(recipeId));
    }

    private void loadRecipe(int id) {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getRecipeById(id).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {

                if (response.isSuccessful() && response.body() != null) {

                    Recipe r = response.body();

                    tvName.setText(r.getName());
                    tvDescription.setText(r.getDescription());
                    tvTime.setText("Tiempo: " + r.getPreparationTime() + " min");

                } else {
                    Toast.makeText(RecipeDetailActivity.this,
                            "Error al cargar receta",
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

    private void addFavorite(int recipeId) {

        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.addFavorite(userId, recipeId).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(RecipeDetailActivity.this,
                            "Añadido a favoritos",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RecipeDetailActivity.this,
                            "No se pudo añadir a favoritos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RecipeDetailActivity.this,
                        "Error de red",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.recipespringandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.adapters.RecipeAdapter;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRecipes();
    }

    private void loadRecipes() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAllRecipes().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this,
                            "Error del servidor: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Recipe> recipes = response.body();

                if (recipes == null || recipes.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "No hay recetas disponibles",
                            Toast.LENGTH_SHORT).show();

                    recyclerView.setAdapter(
                            new RecipeAdapter(new ArrayList<>(), recipe -> {})
                    );
                    return;
                }

                RecipeAdapter adapter = new RecipeAdapter(recipes, recipe -> {

                    if (recipe == null || recipe.getId() == null) {
                        Toast.makeText(MainActivity.this,
                                "Receta inválida",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                    intent.putExtra("recipeId", recipe.getId());
                    startActivity(intent);
                });

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

                Log.e("MAIN_ERROR", "Error cargando recetas", t);

                Toast.makeText(MainActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
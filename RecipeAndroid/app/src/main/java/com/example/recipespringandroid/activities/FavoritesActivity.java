package com.example.recipespringandroid.activities;

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
import com.example.recipespringandroid.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SessionManager sessionManager;

    private RecipeAdapter adapter;
    private List<Recipe> favoritesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sessionManager = new SessionManager(this);

        adapter = new RecipeAdapter(favoritesList, recipe -> {
            Toast.makeText(this,
                    "Receta: " + recipe.getName(),
                    Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {

        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Usuario no logueado", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getFavorites(userId).enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    favoritesList = response.body();

                    Log.d("FAV", "Favoritos: " + favoritesList.size());

                    adapter.updateData(favoritesList);

                } else {
                    Toast.makeText(FavoritesActivity.this,
                            "Error al cargar favoritos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("FAV", t.getMessage());
                Toast.makeText(FavoritesActivity.this,
                        "Error de red",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
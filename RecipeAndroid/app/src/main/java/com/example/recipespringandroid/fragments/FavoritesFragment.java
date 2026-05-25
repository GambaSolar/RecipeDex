package com.example.recipespringandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;

    private int userId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recyclerFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadFavorites();

        return view;
    }

    private void loadFavorites() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getFavorites(userId).enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(),
                            "Error cargando favoritos",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Recipe> favorites = response.body();

                if (favorites.isEmpty()) {
                    recyclerView.setAdapter(
                            new RecipeAdapter(new ArrayList<>(), r -> {})
                    );
                    return;
                }

                RecipeAdapter adapter = new RecipeAdapter(favorites, recipe -> {

                    Toast.makeText(getContext(),
                            recipe.getName(),
                            Toast.LENGTH_SHORT).show();

                });

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
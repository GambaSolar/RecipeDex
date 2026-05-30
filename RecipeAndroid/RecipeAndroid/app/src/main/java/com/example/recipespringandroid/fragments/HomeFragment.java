package com.example.recipespringandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.activities.RecipeDetailActivity;
import com.example.recipespringandroid.adapters.RecipeAdapter;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<Recipe> recipeList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecipeAdapter(recipeList, recipe -> {

            if (recipe == null || recipe.getId() == null) {
                Toast.makeText(getContext(),
                        "Receta inválida",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("recipeId", recipe.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        loadRecipes();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecipes();
    }

    private void loadRecipes() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAllRecipes().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                Log.e("RECIPES_DEBUG", "CODE: " + response.code());

                if (response.errorBody() != null) {
                    try {
                        Log.e("RECIPES_DEBUG", response.errorBody().string());
                    } catch (Exception ignored) {}
                }

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(),
                            "Error cargando recetas",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                recipeList = response.body();
                adapter.updateData(recipeList);

                Log.e("RECIPES_DEBUG", "URL: " + call.request().url());
                Log.e("RECIPES_DEBUG", "CODE: " + response.code());
                Log.e("RECIPES_DEBUG", "MESSAGE: " + response.message());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("RECIPES_ERROR", "FAILURE", t);
                Toast.makeText(getContext(),
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
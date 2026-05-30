package com.example.recipespringandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.activities.RecipeDetailActivity;
import com.example.recipespringandroid.adapters.IngredientAdapter;
import com.example.recipespringandroid.adapters.RecipeAdapter;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.models.Ingredient;
import com.example.recipespringandroid.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText etName, etMaxTime;
    private RecyclerView recyclerResults, recyclerIngredients;
    private Button btnSearch;

    private List<Integer> selectedIngredients = new ArrayList<>();

    private ApiService api;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etName = view.findViewById(R.id.etSearchName);
        etMaxTime = view.findViewById(R.id.etMaxTime);
        btnSearch = view.findViewById(R.id.btnSearch);

        recyclerResults = view.findViewById(R.id.recyclerResults);
        recyclerIngredients = view.findViewById(R.id.recyclerIngredients);

        recyclerResults.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerIngredients.setLayoutManager(new LinearLayoutManager(getContext()));

        api = ApiClient.getClient().create(ApiService.class);

        loadIngredients();

        btnSearch.setOnClickListener(v -> searchRecipes());

        return view;
    }

    private void loadIngredients() {

        api.getAllIngredients().enqueue(new Callback<List<Ingredient>>() {

            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                IngredientAdapter adapter = new IngredientAdapter(
                        response.body(),
                        ids -> selectedIngredients = ids
                );

                recyclerIngredients.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error cargando ingredientes",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchRecipes() {

        String name = etName.getText().toString().trim().toLowerCase();

        Integer maxTime = null;

        try {
            String timeStr = etMaxTime.getText().toString().trim();
            if (!timeStr.isEmpty()) {
                maxTime = Integer.parseInt(timeStr);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Tiempo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer finalMaxTime = maxTime;

        api.getAllRecipes().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                List<Recipe> allRecipes = response.body();
                List<Recipe> result = new ArrayList<>();

                if (allRecipes.isEmpty()) {
                    showResults(result);
                    return;
                }

                final int[] pending = {allRecipes.size()};

                for (Recipe r : allRecipes) {

                    api.getIngredientsByRecipe(r.getId())
                            .enqueue(new Callback<List<Integer>>() {

                                @Override
                                public void onResponse(Call<List<Integer>> call,
                                                       Response<List<Integer>> responseIng) {

                                    List<Integer> ingredients = responseIng.body();
                                    r.setIngredientIds(ingredients);

                                    boolean matches = true;

                                    if (!name.isEmpty()) {
                                        if (r.getName() == null ||
                                                !r.getName().toLowerCase().contains(name)) {
                                            matches = false;
                                        }
                                    }

                                    if (finalMaxTime != null) {
                                        if (r.getPreparationTime() > finalMaxTime) {
                                            matches = false;
                                        }
                                    }

                                    if (!selectedIngredients.isEmpty()) {

                                        if (ingredients == null || ingredients.isEmpty()) {
                                            matches = false;
                                        } else {

                                            boolean ok = false;

                                            for (Integer id : selectedIngredients) {
                                                if (ingredients.contains(id)) {
                                                    ok = true;
                                                    break;
                                                }
                                            }

                                            if (!ok) matches = false;
                                        }
                                    }

                                    if (matches) {
                                        result.add(r);
                                    }

                                    pending[0]--;

                                    if (pending[0] == 0) {
                                        showResults(result);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Integer>> call, Throwable t) {
                                    pending[0]--;
                                    if (pending[0] == 0) {
                                        showResults(result);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResults(List<Recipe> list) {

        RecipeAdapter adapter = new RecipeAdapter(
                list,
                recipe -> {
                    Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                    intent.putExtra("recipeId", recipe.getId());
                    startActivity(intent);
                }
        );

        recyclerResults.setAdapter(adapter);
    }
}
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
import com.example.recipespringandroid.adapters.RecipeAdapter;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private EditText etName, etMaxTime;
    private RecyclerView recyclerResults;
    private Button btnSearch;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etName = view.findViewById(R.id.etSearchName);
        etMaxTime = view.findViewById(R.id.etMaxTime);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclerResults = view.findViewById(R.id.recyclerResults);

        recyclerResults.setLayoutManager(new LinearLayoutManager(getContext()));

        btnSearch.setOnClickListener(v -> searchRecipes());

        return view;
    }

    private void searchRecipes() {

        String name = etName.getText().toString().trim();
        String timeStr = etMaxTime.getText().toString().trim();

        Integer maxTime = null;

        if (!timeStr.isEmpty()) {
            try {
                maxTime = Integer.parseInt(timeStr);
            } catch (Exception e) {
                Toast.makeText(getContext(),
                        "Tiempo inválido",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }

        final Integer finalMaxTime = maxTime;

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.searchRecipes(name).enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(),
                            "Error en búsqueda",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Recipe> results = response.body();

                if (finalMaxTime != null) {

                    List<Recipe> filtered = new ArrayList<>();

                    for (Recipe r : results) {
                        if (r.getPreparationTime() <= finalMaxTime) {
                            filtered.add(r);
                        }
                    }

                    results = filtered;
                }

                showResults(results);
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
package com.example.recipespringandroid.fragments;

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
import com.example.recipespringandroid.adapters.IngredientAdapter;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.models.Ingredient;
import com.example.recipespringandroid.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRecipeFragment extends Fragment {

    private EditText etName, etDescription, etTime;
    private RecyclerView recyclerIngredients;
    private Button btnCreate;

    private List<Ingredient> ingredientList = new ArrayList<>();
    private List<Integer> selectedIngredients = new ArrayList<>();

    private int userId = 1; // luego lo conectamos al login

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_recipe, container, false);

        etName = view.findViewById(R.id.etName);
        etDescription = view.findViewById(R.id.etDescription);
        etTime = view.findViewById(R.id.etTime);
        recyclerIngredients = view.findViewById(R.id.recyclerIngredients);
        btnCreate = view.findViewById(R.id.btnCreate);

        recyclerIngredients.setLayoutManager(new LinearLayoutManager(getContext()));

        loadIngredients();

        btnCreate.setOnClickListener(v -> createRecipe());

        return view;
    }

    private void loadIngredients() {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getIngredients().enqueue(new Callback<List<Ingredient>>() {

            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {

                if (!response.isSuccessful() || response.body() == null) return;

                ingredientList = response.body();

                IngredientAdapter adapter = new IngredientAdapter(
                        ingredientList,
                        selected -> selectedIngredients = selected
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

    private void createRecipe() {

        String name = etName.getText().toString();
        String desc = etDescription.getText().toString();
        String timeStr = etTime.getText().toString();

        if (name.isEmpty() || timeStr.isEmpty()) {
            Toast.makeText(getContext(),
                    "Completa los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int time = Integer.parseInt(timeStr);

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(desc);
        recipe.setPreparationTime(time);

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.createRecipe(userId, recipe).enqueue(new Callback<Recipe>() {

            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(getContext(),
                            "Error creando receta",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Recipe created = response.body();
                int recipeId = created.getId();

                addIngredients(recipeId);
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addIngredients(int recipeId) {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        for (Integer ingredientId : selectedIngredients) {

            api.addRecipeIngredient(recipeId, ingredientId)
                    .enqueue(new Callback<Void>() {

                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }

        Toast.makeText(getContext(),
                "Receta creada",
                Toast.LENGTH_SHORT).show();
    }
}
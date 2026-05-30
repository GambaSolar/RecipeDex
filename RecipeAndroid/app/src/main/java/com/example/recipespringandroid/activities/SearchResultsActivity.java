package com.example.recipespringandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.adapters.RecipeAdapter;
import com.example.recipespringandroid.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnBack;

    private List<Recipe> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.recyclerResults);
        btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        results = (List<Recipe>) intent.getSerializableExtra("results");

        if (results == null) results = new ArrayList<>();

        RecipeAdapter adapter = new RecipeAdapter(results, recipe -> {

            Intent detail = new Intent(this, RecipeDetailActivity.class);
            detail.putExtra("recipeId", recipe.getId());
            startActivity(detail);
        });

        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
    }
}
package com.example.recipespringandroid.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateRecipeActivity extends AppCompatActivity {

    private EditText etName, etDescription, etTime;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etTime = findViewById(R.id.etTime);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(v -> createRecipe());
    }

    private void createRecipe() {

        SessionManager session = new SessionManager(this);
        int userId = session.getUserId();

        if (userId == -1) {
            Toast.makeText(this, "Usuario no logeado", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String timeStr = etTime.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || timeStr.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int time;
        try {
            time = Integer.parseInt(timeStr);
        } catch (Exception e) {
            Toast.makeText(this, "Tiempo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setPreparationTime(time);

        api.createRecipe(userId, recipe).enqueue(new Callback<Recipe>() {

            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(CreateRecipeActivity.this,
                            "Receta creada",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateRecipeActivity.this,
                            "Error al crear receta",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(CreateRecipeActivity.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
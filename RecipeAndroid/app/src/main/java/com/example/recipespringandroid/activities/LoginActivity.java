package com.example.recipespringandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.api.dto.LoginRequest;
import com.example.recipespringandroid.models.User;
import com.example.recipespringandroid.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);

        LoginRequest request = new LoginRequest(username, password);

        api.login(request).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();

                    SessionManager sessionManager = new SessionManager(LoginActivity.this);

                    sessionManager.saveUser(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail()
                    );

                    Toast.makeText(LoginActivity.this,
                            "Bienvenido " + user.getUsername(),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    String errorMsg = "Login incorrecto";

                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.message();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.e("LOGIN_ERROR", t.getMessage() != null ? t.getMessage() : "error");

                Toast.makeText(LoginActivity.this,
                        "Error de conexión con el servidor",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.recipespringandroid.api.repository;

import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.api.dto.LoginRequest;
import com.example.recipespringandroid.api.dto.RegisterRequest;
import com.example.recipespringandroid.models.User;

import retrofit2.Call;

public class UserRepository {

    private final ApiService apiService;

    public UserRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public Call<User> login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);
        return apiService.login(request);
    }

    public Call<User> register(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        return apiService.register(request);
    }
}
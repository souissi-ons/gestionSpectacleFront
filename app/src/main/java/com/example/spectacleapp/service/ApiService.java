package com.example.spectacleapp.service;

import com.example.spectacleapp.model.LoginRequest;
import com.example.spectacleapp.model.LoginResponse;
import com.example.spectacleapp.model.RegisterRequest;
import com.example.spectacleapp.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest registerRequest);
}
package com.example.spectacleapp.service;

import com.example.spectacleapp.dtos.LieuDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LieuService {
    @GET("api/lieux")
    Call<List<LieuDTO>> getAllLieux();
}
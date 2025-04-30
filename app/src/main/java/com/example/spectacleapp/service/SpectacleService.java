package com.example.spectacleapp.service;

import com.example.spectacleapp.dtos.RubriqueDTO;
import com.example.spectacleapp.dtos.SpectacleDTO;
import com.example.spectacleapp.dtos.SpectacleDateLieuDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SpectacleService {
    @GET("api/spectacles")
    Call<List<SpectacleDTO>> getAllSpectacles();

    @GET("api/spectacles/{id}")
    Call<SpectacleDTO> getSpectacleById(@Path("id") int id);


    @GET("api/spectacles/{id}/dates-lieux")
    Call<List<SpectacleDateLieuDTO>> getDatesLieuxForSpectacle(@Path("id") int id);

    @GET("api/spectacles/{id}/rubriques")
    Call<List<RubriqueDTO>> getRubriquesForSpectacle(@Path("id") int id);

    @GET("api/spectacles/date-lieu/{id}/places")
    Call<Integer> getPlacesRestantes(@Path("id") Long id);


}
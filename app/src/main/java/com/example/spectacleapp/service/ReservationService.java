package com.example.spectacleapp.service;

import androidx.annotation.Nullable;

import com.example.spectacleapp.dtos.ReservationDTO;
import com.example.spectacleapp.model.Reservation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReservationService {
        @POST("api/reservations")
        Call<Reservation> createReservation(
                @Header("Authorization") @Nullable String authToken,
                @Body ReservationDTO reservationDTO
        );
    }

package com.example.spectacleapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static final String BASE_URL = "http://192.168.94.165:8081/";
    private static Retrofit retrofit = null;

    public static SpectacleService getSpectacleService() {
        return getClient().create(SpectacleService.class);
    }

    public static LieuService getLieuService() {
        return getClient().create(LieuService.class);
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }

    public static ReservationService getReservationService() {
        return getClient().create(ReservationService.class);
    }
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ") // Format ISO 8601 avec timezone
            .create();

    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // Format sans millisecondes
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
package com.example.airqualitychecker.services;

import com.example.airqualitychecker.models.AirQualityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface APIService {
    @GET("airquality")
    Call<AirQualityResponse> getAirQuality(
            @Header("X-Api-Key") String apiKey,
            @Query("city") String city
    );
}
package com.example.airqualitychecker.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AirQualityResponse implements Serializable {
    public Pollutant CO;
    public Pollutant NO2;
    public Pollutant O3;
    public Pollutant SO2;
    public Pollutant PM10;

    @SerializedName("PM2.5")
    public Pollutant PM2_5;

    public int overall_aqi;

    public String cityName;
}
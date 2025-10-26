package com.example.airqualitychecker.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.airqualitychecker.adapters.AirQualityAdapter;
import com.example.airqualitychecker.models.AirQualityResponse;
import com.example.airqualitychecker.databinding.ActivityDetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;
    private AirQualityAdapter adapter;
    private AirQualityResponse airQualityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        airQualityData = (AirQualityResponse) getIntent().getSerializableExtra("AIR_QUALITY_DATA");

        if (airQualityData == null) {
            finish();
            return;
        }

        setupToolbar();
        setupRecyclerView();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbarDetails);
        String title = null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            String city = airQualityData.cityName;
            title = city.substring(0, 1).toUpperCase() + city.substring(1);
            getSupportActionBar().setTitle(title);
        }
        binding.txtCityNameHeader.setText("Qualidade do Ar: " + title);
    }

    private void setupRecyclerView() {
        binding.recyclerPollutants.setLayoutManager(new LinearLayoutManager(this));
        List<String> pollutants = new ArrayList<>();
        pollutants.add("CO");
        pollutants.add("NO2");
        pollutants.add("O3");
        pollutants.add("SO2");
        pollutants.add("PM10");
        pollutants.add("PM2.5");

        adapter = new AirQualityAdapter(this, pollutants, airQualityData);
        binding.recyclerPollutants.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
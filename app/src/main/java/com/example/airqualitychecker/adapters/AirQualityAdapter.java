package com.example.airqualitychecker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airqualitychecker.databinding.ItemPollutantBinding;
import com.example.airqualitychecker.models.AirQualityResponse;

import java.util.List;

public class AirQualityAdapter extends RecyclerView.Adapter<AirQualityAdapter.ViewHolder> {

    private List<String> pollutantList;
    private AirQualityResponse response;
    private Context context;

    public AirQualityAdapter(Context context, List<String> pollutantList, AirQualityResponse response) {
        this.context = context;
        this.pollutantList = pollutantList;
        this.response = response;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPollutantBinding binding = ItemPollutantBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = pollutantList.get(position);
        int aqi = 0;

        if (response != null) {
            switch (name) {
                case "CO":
                    if (response.CO != null) aqi = response.CO.aqi;
                    break;
                case "NO2":
                    if (response.NO2 != null) aqi = response.NO2.aqi;
                    break;
                case "O3":
                    if (response.O3 != null) aqi = response.O3.aqi;
                    break;
                case "SO2":
                    if (response.SO2 != null) aqi = response.SO2.aqi;
                    break;
                case "PM10":
                    if (response.PM10 != null) aqi = response.PM10.aqi;
                    break;
                case "PM2.5":
                    if (response.PM2_5 != null) aqi = response.PM2_5.aqi;
                    break;
            }
        }

        holder.binding.txtName.setText(name);
        holder.binding.txtValue.setText("AQI: " + aqi);

    }

    @Override
    public int getItemCount() {
        return pollutantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPollutantBinding binding;

        public ViewHolder(ItemPollutantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
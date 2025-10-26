package com.example.airqualitychecker.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airqualitychecker.R;
import com.example.airqualitychecker.activities.DetailsActivity;
import com.example.airqualitychecker.databinding.ItemSearchResultBinding;
import com.example.airqualitychecker.models.AirQualityResponse;
import com.example.airqualitychecker.utils.FavoritesManager;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultViewHolder> {

    private List<AirQualityResponse> results;
    private Context context;
    private FavoritesManager favoritesManager;
    private Runnable onFavoriteChangedCallback;

    public SearchResultAdapter(Context context, List<AirQualityResponse> results, FavoritesManager manager, Runnable callback) {
        this.context = context;
        this.results = results;
        this.favoritesManager = manager;
        this.onFavoriteChangedCallback = callback;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ResultViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        AirQualityResponse data = results.get(position);
        holder.bind(data, context, favoritesManager, onFavoriteChangedCallback);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    // Helper para o status
    private static String getAqiStatus(int aqi) {
        if (aqi <= 50) return "Qualidade Boa";
        if (aqi <= 100) return "Qualidade Mediana";
        if (aqi <= 150) return "Ruim (Grupos Sensíveis)";
        return "Qualidade Ruim";
    }

    // Helper para a cor do status
    private static int getAqiColor(Context context, int aqi) {
        if (aqi <= 50) return ContextCompat.getColor(context, R.color.statusGood);
        if (aqi <= 100) return ContextCompat.getColor(context, R.color.statusModerate);
        return ContextCompat.getColor(context, R.color.statusBad);
    }


    static class ResultViewHolder extends RecyclerView.ViewHolder {
        private ItemSearchResultBinding binding;

        public ResultViewHolder(ItemSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AirQualityResponse data, Context context, FavoritesManager manager, Runnable callback) {
            binding.txtCityName.setText(data.cityName);
            binding.txtAqiValue.setText("(AQI: " + data.overall_aqi + ")");
            binding.txtAqiStatus.setText(getAqiStatus(data.overall_aqi));

            int color = getAqiColor(context, data.overall_aqi);
            binding.txtAqiStatus.setTextColor(color);
            binding.btnToggleFavorite.setColorFilter(color);

            updateFavoriteIcon(manager.isFavorite(data.cityName));

            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("AIR_QUALITY_DATA", data);
                context.startActivity(intent);
            });

            binding.btnToggleFavorite.setOnClickListener(v -> {
                if (manager.isFavorite(data.cityName)) {
                    manager.removeFavorite(data.cityName);
                    updateFavoriteIcon(false);
                } else {
                    manager.addFavorite(data.cityName);
                    updateFavoriteIcon(true);
                }
                callback.run();
            });
        }

        private void updateFavoriteIcon(boolean isFavorite) {
            if (isFavorite) {
                binding.btnToggleFavorite.setImageResource(R.drawable.baseline_star_24);
            } else {
                binding.btnToggleFavorite.setImageResource(R.drawable.baseline_star_border_24);
            }
        }
    }
}
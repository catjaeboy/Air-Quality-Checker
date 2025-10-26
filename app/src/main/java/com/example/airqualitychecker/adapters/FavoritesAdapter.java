package com.example.airqualitychecker.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airqualitychecker.databinding.ItemFavoriteBinding;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private List<String> favoriteCities;
    private OnFavoriteClickListener listener;

    // Interface para cliques
    public interface OnFavoriteClickListener {
        void onCityClick(String city);
        void onDeleteClick(String city);
    }

    public FavoritesAdapter(List<String> favoriteCities, OnFavoriteClickListener listener) {
        this.favoriteCities = favoriteCities;
        this.listener = listener;
    }

    public void updateFavorites(List<String> newFavorites) {
        this.favoriteCities = newFavorites;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteBinding binding = ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        String city = favoriteCities.get(position);
        holder.bind(city, listener);
    }

    @Override
    public int getItemCount() {
        return favoriteCities.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ItemFavoriteBinding binding;

        public FavoriteViewHolder(ItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String city, OnFavoriteClickListener listener) {
            String displayName = city.substring(0, 1).toUpperCase() + city.substring(1);
            binding.txtCityName.setText(displayName);

            binding.btnDeleteFavorite.setOnClickListener(v -> listener.onDeleteClick(city));

            binding.layoutFavoriteRoot.setOnClickListener(v -> listener.onCityClick(city));
        }
    }
}
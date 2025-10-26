package com.example.airqualitychecker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoritesManager {

    private static final String PREFS_NAME = "AirPrefs";
    private static final String KEY_FAVORITES = "favorite_cities";
    private SharedPreferences prefs;

    public FavoritesManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public Set<String> getFavorites() {
        // Retorna uma cópia para evitar modificação acidental
        return new HashSet<>(prefs.getStringSet(KEY_FAVORITES, new HashSet<>()));
    }

    public void addFavorite(String city) {
        Set<String> favorites = getFavorites();
        favorites.add(city.toLowerCase()); // Salva em minúsculas para evitar duplicatas
        prefs.edit().putStringSet(KEY_FAVORITES, favorites).apply();
    }

    public void removeFavorite(String city) {
        Set<String> favorites = getFavorites();
        favorites.remove(city.toLowerCase());
        prefs.edit().putStringSet(KEY_FAVORITES, favorites).apply();
    }

    public boolean isFavorite(String city) {
        return getFavorites().contains(city.toLowerCase());
    }
}
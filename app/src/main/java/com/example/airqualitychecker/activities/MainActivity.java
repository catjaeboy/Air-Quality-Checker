package com.example.airqualitychecker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.airqualitychecker.services.APIService;
import com.example.airqualitychecker.models.AirQualityResponse;
import com.example.airqualitychecker.adapters.FavoritesAdapter;
import com.example.airqualitychecker.utils.FavoritesManager;
import com.example.airqualitychecker.services.RetrofitClient;
import com.example.airqualitychecker.adapters.SearchResultAdapter;
import com.example.airqualitychecker.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private APIService apiService;
    private FavoritesManager favoritesManager;
    private SearchResultAdapter searchAdapter;
    private FavoritesAdapter favoritesAdapter;

    private final String apiKey = "c2SxhpjjqSG5zz2HduM7mQ==dMr6i2vjSEX4C2ry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar); // Define a Toolbar

        // Inicializar
        apiService = RetrofitClient.getClient().create(APIService.class);
        favoritesManager = new FavoritesManager(this);

        setupRecyclerViews();
        loadFavoriteCities();

        // Configurar o botão de busca
        binding.btnSearch.setOnClickListener(v -> {
            String city = binding.editCity.getText().toString().trim();
            if (!city.isEmpty()) {
                searchAirQuality(city);
            } else {
                Toast.makeText(MainActivity.this, "Por favor, digite uma cidade.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerViews() {
        // --- Adapter do Resultado da Busca ---
        binding.recyclerSearchResult.setLayoutManager(new LinearLayoutManager(this));
        // Criar adapter com uma lista vazia
        searchAdapter = new SearchResultAdapter(this, new ArrayList<>(), favoritesManager, () -> {
            // Este é o "callback" que é chamado quando um favorito é alterado
            loadFavoriteCities();
        });
        binding.recyclerSearchResult.setAdapter(searchAdapter);

        // --- Adapter dos Favoritos ---
        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        favoritesAdapter = new FavoritesAdapter(new ArrayList<>(), new FavoritesAdapter.OnFavoriteClickListener() {
            @Override
            public void onCityClick(String city) {
                // Ao clicar em um favorito, pesquisa por ele
                binding.editCity.setText(city);
                searchAirQuality(city);
            }

            @Override
            public void onDeleteClick(String city) {
                // Deleta o favorito e atualiza as duas listas
                favoritesManager.removeFavorite(city);
                loadFavoriteCities();
                searchAirQuality(binding.editCity.getText().toString().trim()); // Atualiza o ícone de estrela da busca atual
            }
        });
        binding.recyclerFavorites.setAdapter(favoritesAdapter);
    }

    private void loadFavoriteCities() {
        List<String> favList = new ArrayList<>(favoritesManager.getFavorites());
        Collections.sort(favList); // Ordena alfabeticamente

        if (favList.isEmpty()) {
            binding.txtFavoritesLabel.setVisibility(View.GONE);
        } else {
            binding.txtFavoritesLabel.setVisibility(View.VISIBLE);
        }

        favoritesAdapter.updateFavorites(favList);
    }

    private void searchAirQuality(String city) {
        if (city.isEmpty()) return; // Não pesquisa se o campo estiver vazio

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.txtSearchLabel.setVisibility(View.GONE);
        binding.recyclerSearchResult.setVisibility(View.GONE);

        apiService.getAirQuality(apiKey, city).enqueue(new Callback<AirQualityResponse>() {
            @Override
            public void onResponse(Call<AirQualityResponse> call, Response<AirQualityResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    AirQualityResponse data = response.body();

                    // Verifica se a API retornou um objeto vazio ou com erro
                    if (data.CO == null && data.overall_aqi == 0) {
                        Toast.makeText(MainActivity.this, "Cidade não encontrada ou sem dados.", Toast.LENGTH_SHORT).show();
                        binding.txtSearchLabel.setVisibility(View.GONE);
                        binding.recyclerSearchResult.setVisibility(View.GONE);
                        return;
                    }

                    data.cityName = city; // Salva o nome da cidade no objeto de resposta

                    // Atualiza o adapter de busca
                    List<AirQualityResponse> resultList = new ArrayList<>();
                    resultList.add(data);

                    searchAdapter = new SearchResultAdapter(MainActivity.this, resultList, favoritesManager, () -> loadFavoriteCities());
                    binding.recyclerSearchResult.setAdapter(searchAdapter);

                    binding.txtSearchLabel.setVisibility(View.VISIBLE);
                    binding.recyclerSearchResult.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(MainActivity.this, "Cidade inválida!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AirQualityResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Ocorreu um Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
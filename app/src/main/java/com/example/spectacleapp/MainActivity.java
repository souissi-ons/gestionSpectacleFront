package com.example.spectacleapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.activities.LoginActivity;
import com.example.spectacleapp.adapter.SpectacleListAdapter;
import com.example.spectacleapp.dtos.LieuDTO;
import com.example.spectacleapp.dtos.SpectacleDTO;
import com.example.spectacleapp.dtos.SpectacleDateLieuDTO;
import com.example.spectacleapp.service.ApiClient;
import com.example.spectacleapp.service.LieuService;
import com.example.spectacleapp.service.SpectacleService;
import com.example.spectacleapp.utils.AuthHelper;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SpectacleService spectacleService;
    private SpectacleListAdapter adapter;
    private List<SpectacleDTO> spectacleListFull;
    private Button logoutButton;
    private Button loginButton;
    private AuthHelper authHelper;
    private MaterialAutoCompleteTextView categorySpinner;
    private MaterialAutoCompleteTextView lieuSpinner;
    private List<String> categories = new ArrayList<>();
    private List<LieuDTO> lieux = new ArrayList<>();
    private LieuService lieuService;
    private com.google.android.material.search.SearchBar searchBar;
    private com.google.android.material.search.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        setupSearchView();
        loadSpectaclesFromApi();
        loadCategoriesAndLieux();
        setupLoginButton();
        setupLogoutButton();
        updateAuthButtonsVisibility();
    }

    private void initComponents() {
        spectacleService = ApiClient.getSpectacleService();
        authHelper = new AuthHelper(this);

        // Initialize views
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SpectacleListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        logoutButton = findViewById(R.id.logoutButton);
        loginButton = findViewById(R.id.loginButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        lieuSpinner = findViewById(R.id.lieuSpinner);

        lieuService = ApiClient.getLieuService();
    }

    private void loadCategoriesAndLieux() {
        // Load categories
        categories.add("Toutes catégories");
        categories.add("Théâtre");
        categories.add("Musique");
        categories.add("Danse");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, categories);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemClickListener((parent, view, position, id) -> applyFilters());

        // Load places from API
        lieuService.getAllLieux().enqueue(new Callback<List<LieuDTO>>() {
            @Override
            public void onResponse(Call<List<LieuDTO>> call, Response<List<LieuDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    lieux = response.body();
                    List<String> lieuNames = new ArrayList<>();
                    lieuNames.add("Tous les lieux");
                    for (LieuDTO lieu : lieux) {
                        lieuNames.add(lieu.getNom());
                    }

                    ArrayAdapter<String> lieuAdapter = new ArrayAdapter<>(
                            MainActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            lieuNames);
                    lieuSpinner.setAdapter(lieuAdapter);
                    lieuSpinner.setOnItemClickListener((parent, view, position, id) -> applyFilters());
                } else {
                    showToast("Erreur de chargement des lieux");
                }
            }

            @Override
            public void onFailure(Call<List<LieuDTO>> call, Throwable t) {
                showToast("Erreur de connexion: " + t.getMessage());
                Log.e("API Error", "Erreur de chargement des lieux", t);
            }
        });
    }

    private void setupSearchView() {
        searchBar = findViewById(R.id.searchBar);
        searchView = findViewById(R.id.searchView);

        searchView.setupWithSearchBar(searchBar);

        // Configurez l'EditText du SearchView
        EditText editText = searchView.getEditText();
        if (editText != null) {
            editText.setHint("Rechercher un spectacle...");
            editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

            // Gestion de la recherche
            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = editText.getText().toString();
                    filterSpectacles(query);
                    searchBar.setText(query); // Met à jour le texte dans la SearchBar
                    searchView.hide();
                    return true;
                }
                return false;
            });

            // Mise à jour en temps réel
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchBar.setText(s.toString()); // Met à jour en temps réel
                    filterSpectacles(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        // Gestion du clic sur la SearchBar
        searchBar.setOnClickListener(v -> {
            searchView.show();
            searchView.getEditText().requestFocus();
        });
    }

    private void applyFilters() {
        if (spectacleListFull == null) return;

        String selectedCategory = categorySpinner.getText().toString();
        String selectedLieu = lieuSpinner.getText().toString();

        List<SpectacleDTO> filteredList = new ArrayList<>();

        for (SpectacleDTO spectacle : spectacleListFull) {
            boolean matchesCategory = selectedCategory.equals("Toutes catégories") ||
                    (spectacle.getCategorie() != null &&
                            spectacle.getCategorie().equalsIgnoreCase(selectedCategory));

            boolean matchesLieu = selectedLieu.equals("Tous les lieux");

            if (!matchesLieu && spectacle.getDatesLieux() != null) {
                for (SpectacleDateLieuDTO dateLieu : spectacle.getDatesLieux()) {
                    if (dateLieu.getLieu() != null &&
                            dateLieu.getLieu().getNom().equalsIgnoreCase(selectedLieu)) {
                        matchesLieu = true;
                        break;
                    }
                }
            }

            if (matchesCategory && matchesLieu) {
                filteredList.add(spectacle);
            }
        }

        adapter.setSpectacles(filteredList);
    }

    private void loadSpectaclesFromApi() {
        spectacleService.getAllSpectacles().enqueue(new Callback<List<SpectacleDTO>>() {
            @Override
            public void onResponse(Call<List<SpectacleDTO>> call, Response<List<SpectacleDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    spectacleListFull = response.body();
                    adapter.setSpectacles(spectacleListFull);
                } else {
                    showToast("Aucun spectacle trouvé");
                }
            }

            @Override
            public void onFailure(Call<List<SpectacleDTO>> call, Throwable t) {
                showToast("Erreur de connexion: " + t.getMessage());
                Log.e("API Error", "Erreur de chargement", t);
            }
        });
    }

    private void filterSpectacles(String query) {
        if (spectacleListFull == null || query.isEmpty()) {
            adapter.setSpectacles(spectacleListFull);
            return;
        }

        List<SpectacleDTO> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase(Locale.getDefault());

        for (SpectacleDTO spectacle : spectacleListFull) {
            if (matchesSearchQuery(spectacle, lowerCaseQuery)) {
                filteredList.add(spectacle);
            }
        }

        adapter.setSpectacles(filteredList);
    }

    private boolean matchesSearchQuery(SpectacleDTO spectacle, String query) {
        if (spectacle.getTitre() != null && spectacle.getTitre().toLowerCase().contains(query)) {
            return true;
        }

        if (spectacle.getDescription() != null && spectacle.getDescription().toLowerCase().contains(query)) {
            return true;
        }

        if (spectacle.getDatesLieux() != null) {
            for (SpectacleDateLieuDTO dateLieu : spectacle.getDatesLieux()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String dateStr = dateLieu.getDate() != null ? dateFormat.format(dateLieu.getDate()) : "";

                if (dateStr.toLowerCase().contains(query)) {
                    return true;
                }

                if (dateLieu.getLieu() != null) {
                    if (dateLieu.getLieu().getNom() != null &&
                            dateLieu.getLieu().getNom().toLowerCase().contains(query)) {
                        return true;
                    }

                    if (dateLieu.getLieu().getAdresse() != null &&
                            dateLieu.getLieu().getAdresse().toLowerCase().contains(query)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void setupLoginButton() {
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void setupLogoutButton() {
        logoutButton.setOnClickListener(v -> {
            authHelper.clearUserData();
            Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
            updateAuthButtonsVisibility();
        });
    }

    private void updateAuthButtonsVisibility() {
        runOnUiThread(() -> {
            boolean isLoggedIn = authHelper.isUserLoggedIn();
            logoutButton.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);
            loginButton.setVisibility(isLoggedIn ? View.GONE : View.VISIBLE);
            Log.d("AuthState", "Utilisateur connecté: " + isLoggedIn);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAuthButtonsVisibility();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
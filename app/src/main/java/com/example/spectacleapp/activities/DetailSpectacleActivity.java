package com.example.spectacleapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleapp.R;
import com.example.spectacleapp.adapter.DateLieuAdapter;
import com.example.spectacleapp.dtos.ArtisteDTO;
import com.example.spectacleapp.dtos.LieuDTO;
import com.example.spectacleapp.dtos.RubriqueDTO;
import com.example.spectacleapp.dtos.SpectacleDTO;
import com.example.spectacleapp.dtos.SpectacleDateLieuDTO;
import com.example.spectacleapp.model.Lieu;
import com.example.spectacleapp.service.ApiClient;
import com.example.spectacleapp.service.SpectacleService;
import com.example.spectacleapp.utils.AuthHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailSpectacleActivity extends AppCompatActivity {

    private SpectacleService spectacleService;
    private AuthHelper authHelper;

    private LinearLayout rubriquesContainer;
    private RecyclerView datesLieuxRecyclerView;
    private MaterialButton reservationButton;

    private DateLieuAdapter dateLieuAdapter;
    private SpectacleDateLieuDTO selectedDateLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_spectacle);

        initViews();
        setupRecyclerView();
        loadSpectacleData();
        setupReservationButton();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

    }

    private void initViews() {
        spectacleService = ApiClient.getSpectacleService();
        authHelper = new AuthHelper(this);

        rubriquesContainer = findViewById(R.id.rubriques_container);
        datesLieuxRecyclerView = findViewById(R.id.dates_lieux_recycler);
        reservationButton = findViewById(R.id.reservation_button);

        // Désactiver le bouton tant qu'aucune date n'est sélectionnée
        reservationButton.setEnabled(false);
    }

    private void setupRecyclerView() {
        datesLieuxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dateLieuAdapter = new DateLieuAdapter(null, new DateLieuAdapter.OnDateLieuClickListener() {
            @Override
            public void onReservationClick(SpectacleDateLieuDTO dateLieu) {
                handleReservation(dateLieu);
            }

            @Override
            public void onLieuClick(String adresse) {
                openLocationInMaps(adresse);
            }

            @Override
            public void onItemClick(SpectacleDateLieuDTO dateLieu) {
                updateSelectedDateLieuUI(dateLieu);
            }
        });
        datesLieuxRecyclerView.setAdapter(dateLieuAdapter);
    }

    private void setupReservationButton() {
        reservationButton.setOnClickListener(view -> {
            if (selectedDateLieu != null) {
                handleReservation(selectedDateLieu);
            } else {
                Toast.makeText(this, "Veuillez sélectionner une date.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleReservation(SpectacleDateLieuDTO dateLieu) {
        if (dateLieu.getLieu() == null) {
            Toast.makeText(this, "Information sur le lieu manquante", Toast.LENGTH_SHORT).show();
            return;
        }

        Lieu lieu = convertLieuDtoToLieu(dateLieu.getLieu());

        if (authHelper.isUserLoggedIn()) {
            // Utilisateur connecté - aller directement à ReservationLoggedInActivity
            Intent intent = new Intent(this, ReservationLoggedInActivity.class);
            intent.putExtra("SPECTACLE_ID", getIntent().getIntExtra("SPECTACLE_ID", -1));
            intent.putExtra("SPECTACLE_TITRE", ((TextView) findViewById(R.id.detail_titre)).getText().toString());
            intent.putExtra("DATE_LIEU_ID", dateLieu.getId());
            intent.putExtra("PLACES_DISPONIBLES", dateLieu.getPlacesDisponibles());
            intent.putExtra("PRIX", dateLieu.getPrix());
            intent.putExtra("LIEU_ID", lieu.getIdLieu());
            intent.putExtra("LIEU_NOM", lieu.getNomLieu());
            intent.putExtra("LIEU_ADRESSE", lieu.getAdresse());
            intent.putExtra("DATE_SPECTACLE", dateLieu.getDate().toString());
            intent.putExtra("HEURE_DEBUT", dateLieu.getHeureDebut());
            startActivity(intent);
        } else {
            // Utilisateur non connecté - préparer l'intent pour ReservationActivity
            Intent intent = new Intent(this, ReservationActivity.class);
            intent.putExtra("SPECTACLE_ID", getIntent().getIntExtra("SPECTACLE_ID", -1));
            intent.putExtra("SPECTACLE_TITRE", ((TextView) findViewById(R.id.detail_titre)).getText().toString());
            intent.putExtra("DATE_LIEU_ID", dateLieu.getId());
            intent.putExtra("PLACES_DISPONIBLES", dateLieu.getPlacesDisponibles());
            intent.putExtra("PRIX", dateLieu.getPrix());
            intent.putExtra("LIEU_ID", lieu.getIdLieu());
            intent.putExtra("LIEU_NOM", lieu.getNomLieu());
            intent.putExtra("LIEU_ADRESSE", lieu.getAdresse());
            intent.putExtra("DATE_SPECTACLE", dateLieu.getDate().toString());
            intent.putExtra("HEURE_DEBUT", dateLieu.getHeureDebut());
            showReservationOptionsDialog(intent);
        }
    }

    private void showReservationOptionsDialog(Intent reservationIntent) {
        new AlertDialog.Builder(this)
                .setTitle("Options de réservation")
                .setMessage("Vous pouvez réserver avec ou sans compte. Un compte vous permet de gérer plus facilement vos réservations.")

                .setPositiveButton("Créer un compte", (dialog, which) -> {
                    Intent signupIntent = new Intent(this, SignupActivity.class);
                    signupIntent.putExtras(reservationIntent.getExtras());
                    signupIntent.putExtra("REDIRECT_TO_RESERVATION", true);
                    startActivity(signupIntent);
                })
                .setNegativeButton("Continuer sans compte", (dialog, which) -> {
                    // Ajouter un flag pour indiquer que c'est une réservation sans compte
                    reservationIntent.putExtra("RESERVATION_SANS_COMPTE", true);
                    startActivity(reservationIntent);
                })
                .setNeutralButton("Se connecter", (dialog, which) -> {
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    loginIntent.putExtras(reservationIntent.getExtras());
                    loginIntent.putExtra("REDIRECT_TO_RESERVATION", true);
                    startActivity(loginIntent);
                })
                .show();
    }
    private void updateSelectedDateLieuUI(SpectacleDateLieuDTO dateLieu) {
        selectedDateLieu = dateLieu;
        reservationButton.setEnabled(selectedDateLieu != null);
    }

    private void loadSpectacleData() {
        int spectacleId = getIntent().getIntExtra("SPECTACLE_ID", -1);
        if (spectacleId != -1) {
            loadSpectacleDetailsFromApi(spectacleId);
        } else {
            showError("ID de spectacle invalide");
        }
    }

    private void loadSpectacleDetailsFromApi(int spectacleId) {
        spectacleService.getSpectacleById(spectacleId).enqueue(new Callback<SpectacleDTO>() {
            @Override
            public void onResponse(Call<SpectacleDTO> call, Response<SpectacleDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SpectacleDTO spectacle = response.body();
                    updateUI(spectacle);

                    if (spectacle.getRubriques() == null || spectacle.getRubriques().isEmpty()) {
                        loadRubriquesFromApi(spectacleId);
                    } else {
                        displayRubriques(spectacle.getRubriques());
                    }

                    if (spectacle.getDatesLieux() == null || spectacle.getDatesLieux().isEmpty()) {
                        loadDatesLieuxFromApi(spectacleId);
                    } else {
                        // Charger les places disponibles pour chaque date/lieu
                        for (SpectacleDateLieuDTO dateLieu : spectacle.getDatesLieux()) {
                            loadPlacesRestantes(dateLieu);
                        }
                        dateLieuAdapter.updateData(spectacle.getDatesLieux());
                        if (!spectacle.getDatesLieux().isEmpty()) {
                            updateSelectedDateLieuUI(spectacle.getDatesLieux().get(0));
                        }
                    }
                } else {
                    showError("Détails du spectacle non disponibles");
                }
            }

            @Override
            public void onFailure(Call<SpectacleDTO> call, Throwable t) {
                showError("Erreur réseau: " + t.getMessage());
            }
        });
    }

    private void loadDatesLieuxFromApi(int spectacleId) {
        spectacleService.getDatesLieuxForSpectacle(spectacleId).enqueue(
                new Callback<List<SpectacleDateLieuDTO>>() {
                    @Override
                    public void onResponse(Call<List<SpectacleDateLieuDTO>> call,
                                           Response<List<SpectacleDateLieuDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<SpectacleDateLieuDTO> datesLieux = response.body();

                            // Pour chaque date/lieu, charger les places restantes
                            for (SpectacleDateLieuDTO dateLieu : datesLieux) {
                                loadPlacesRestantes(dateLieu);
                            }

                            dateLieuAdapter.updateData(datesLieux);
                            if (!datesLieux.isEmpty()) {
                                updateSelectedDateLieuUI(datesLieux.get(0));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SpectacleDateLieuDTO>> call, Throwable t) {
                        Log.e("API_FAIL", "Erreur réseau: " + t.getMessage());
                    }
                });
    }

    private void loadPlacesRestantes(SpectacleDateLieuDTO dateLieu) {
        spectacleService.getPlacesRestantes(dateLieu.getId()).enqueue(
                new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.e("API_Call", "Response: " + response.body());
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("API_RESPONSE", "Places for ID " + dateLieu.getId() +
                                    ": " + response.body());
                            dateLieu.setPlacesDisponibles(response.body());
                            // Notifier l'adaptateur du changement
                            runOnUiThread(() -> {
                                dateLieuAdapter.notifyItemChanged(
                                        dateLieuAdapter.getCurrentList().indexOf(dateLieu)
                                );
                            });
                        } else {
                            Log.e("API_ERROR", "Response not successful: " + response.code() + " " + dateLieu.getId());
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.e("API_FAILURE", "Error for ID " + dateLieu.getId(), t);
                        runOnUiThread(() -> {
                            dateLieuAdapter.notifyItemChanged(
                                    dateLieuAdapter.getCurrentList().indexOf(dateLieu)
                            );
                        });
                    }
                });
    }

    private void loadRubriquesFromApi(int spectacleId) {
        spectacleService.getRubriquesForSpectacle(spectacleId).enqueue(new Callback<List<RubriqueDTO>>() {
            @Override
            public void onResponse(Call<List<RubriqueDTO>> call, Response<List<RubriqueDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayRubriques(response.body());
                } else {
                    Toast.makeText(DetailSpectacleActivity.this, "Erreur de chargement des rubriques", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RubriqueDTO>> call, Throwable t) {
                Toast.makeText(DetailSpectacleActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(SpectacleDTO spectacle) {
        ImageView imageView = findViewById(R.id.detail_image);
        TextView titreView = findViewById(R.id.detail_titre);
        TextView descriptionView = findViewById(R.id.detail_description);

        if (spectacle.getImageUrl() != null) {
            loadImageResource(spectacle.getImageUrl(), imageView);
        } else {
            imageView.setImageResource(R.drawable.default_image);
        }

        titreView.setText(spectacle.getTitre());
        descriptionView.setText(spectacle.getDescription() != null ? spectacle.getDescription() : "Description non disponible");
    }

    private void displayRubriques(List<RubriqueDTO> rubriques) {
        rubriquesContainer.removeAllViews();
        if (rubriques == null || rubriques.isEmpty()) {
            TextView noRubriques = new TextView(this);
            noRubriques.setText("Aucune rubrique pour ce spectacle");
            rubriquesContainer.addView(noRubriques);
            return;
        }

        for (RubriqueDTO rubrique : rubriques) {
            View rubriqueView = LayoutInflater.from(this).inflate(R.layout.item_rubrique, rubriquesContainer, false);

            TextView rubriqueInfo = rubriqueView.findViewById(R.id.rubrique_info);
            TextView artisteInfo = rubriqueView.findViewById(R.id.artiste_info);
            TextView horaireInfo = rubriqueView.findViewById(R.id.horaire_info);

            rubriqueInfo.setText(rubrique.getType() != null && !rubrique.getType().isEmpty() ? rubrique.getType() : "Type non spécifié");
            artisteInfo.setText(formatArtisteInfo(rubrique.getArtiste()));
            horaireInfo.setText(formatHeureRubrique(rubrique.getHDebutR(), rubrique.getDureeRub()));

            rubriquesContainer.addView(rubriqueView);
        }
    }

    private void openLocationInMaps(String address) {
        try {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Uri webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(address));
                startActivity(new Intent(Intent.ACTION_VIEW, webUri));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Impossible d'ouvrir la carte", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatArtisteInfo(ArtisteDTO artiste) {
        if (artiste == null) return "Artiste non spécifié";
        StringBuilder builder = new StringBuilder();
        if (artiste.getPrenom() != null) builder.append(artiste.getPrenom()).append(" ");
        if (artiste.getNom() != null) builder.append(artiste.getNom());
        if (artiste.getSpecialite() != null) builder.append(" - ").append(artiste.getSpecialite());
        return builder.toString().trim();
    }

    private String formatHeureRubrique(double heureDebut, double duree) {
        try {
            int heuresDebut = (int) heureDebut;
            int minutesDebut = (int) Math.round((heureDebut - heuresDebut) * 60);
            String debutStr = String.format(Locale.FRANCE, "%02dh%02d", heuresDebut, minutesDebut);

            if (duree <= 0) return debutStr;

            double heureFin = heureDebut + duree;
            int heuresFin = (int) heureFin;
            int minutesFin = (int) Math.round((heureFin - heuresFin) * 60);
            String finStr = String.format(Locale.FRANCE, "%02dh%02d", heuresFin, minutesFin);

            return debutStr + " - " + finStr;
        } catch (Exception e) {
            Log.e("FORMAT_HEURE", "Erreur de formatage de l'heure", e);
            return "Horaire indisponible";
        }
    }

    private Lieu convertLieuDtoToLieu(LieuDTO lieuDto) {
        Lieu lieu = new Lieu();
        lieu.setIdLieu(lieuDto.getId());
        lieu.setNomLieu(lieuDto.getNom());
        lieu.setAdresse(lieuDto.getAdresse());
        lieu.setCapacite(lieuDto.getCapacite());
        return lieu;
    }

    private void loadImageResource(String imageName, ImageView imageView) {
        if (imageName == null) {
            imageView.setImageResource(R.drawable.default_image);
            return;
        }
        String resourceName = imageName.split("\\.")[0].toLowerCase(Locale.ROOT).replace("-", "_");
        int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        imageView.setImageResource(resId != 0 ? resId : R.drawable.default_image);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }


}

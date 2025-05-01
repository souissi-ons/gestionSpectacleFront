package com.example.spectacleapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleapp.R;
import com.example.spectacleapp.dtos.ReservationDTO;
import com.example.spectacleapp.service.ApiClient;
import com.example.spectacleapp.service.ReservationService;
import com.example.spectacleapp.utils.AuthHelper;

public class ReservationActivity extends AppCompatActivity {

    private EditText etNom, etPrenom, etEmail, etTelephone, etNbPlaces;
    private Button btnReserver;
    private AuthHelper authHelper;
    private Long spectacleId;
    private Long dateLieuId;
    private int placesDisponibles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        authHelper = new AuthHelper(this);
        spectacleId = getIntent().getLongExtra("SPECTACLE_ID", -1);
        dateLieuId = getIntent().getLongExtra("DATE_LIEU_ID", -1);
        placesDisponibles = getIntent().getIntExtra("PLACES_DISPONIBLES", 0);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etNom = findViewById(R.id.et_nom);
        etPrenom = findViewById(R.id.et_prenom);
        etEmail = findViewById(R.id.et_email);
        etTelephone = findViewById(R.id.et_telephone);
        etNbPlaces = findViewById(R.id.et_nb_places);
        btnReserver = findViewById(R.id.btn_reserver);

        // Pré-remplir si utilisateur connecté
        if (authHelper.isUserLoggedIn()) {
            etEmail.setText(authHelper.getUserEmail());
            etEmail.setEnabled(false);
        }
    }

    private void setupListeners() {
        btnReserver.setOnClickListener(v -> {
            if (validateInputs()) {
                    proceedToPayment();
            }
        });
    }


    private boolean validateInputs() {
        // Validation du nom
        String nom = etNom.getText().toString().trim();
        if (nom.isEmpty()) {
            etNom.setError("Le nom est obligatoire");
            return false;
        } else if (nom.length() < 3) {
            etNom.setError("Le nom doit contenir au moins 3 caractères");
            return false;
        }

        // Validation du prénom
        String prenom = etPrenom.getText().toString().trim();
        if (prenom.isEmpty()) {
            etPrenom.setError("Le prénom est obligatoire");
            return false;
        } else if (prenom.length() < 3) {
            etPrenom.setError("Le prénom doit contenir au moins 3 caractères");
            return false;
        }

        // Validation de l'email
        String email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            etEmail.setError("L'email est obligatoire");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Veuillez entrer un email valide");
            return false;
        }

        // Validation du téléphone
        String telephone = etTelephone.getText().toString().trim();
        if (telephone.isEmpty()) {
            etTelephone.setError("Le téléphone est obligatoire");
            return false;
        } else if (!Patterns.PHONE.matcher(telephone).matches()) {
            etTelephone.setError("Veuillez entrer un numéro valide");
            return false;
        }

        // Validation du nombre de places
        String nbPlacesText = etNbPlaces.getText().toString().trim();
        if (nbPlacesText.isEmpty()) {
            etNbPlaces.setError("Le nombre de places est obligatoire");
            return false;
        }

        try {
            int nbPlaces = Integer.parseInt(nbPlacesText);
            if (nbPlaces <= 0) {
                etNbPlaces.setError("Le nombre de places doit être positif");
                return false;
            }
            if (nbPlaces > placesDisponibles) {
                etNbPlaces.setError("Il ne reste que " + placesDisponibles + " places disponibles");
                return false;
            }
        } catch (NumberFormatException e) {
            etNbPlaces.setError("Veuillez entrer un nombre valide");
            return false;
        }

        return true;
    }

    private void proceedToPayment() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("NOM", etNom.getText().toString());
        intent.putExtra("PRENOM", etPrenom.getText().toString());
        intent.putExtra("EMAIL", etEmail.getText().toString());
        intent.putExtra("TELEPHONE", etTelephone.getText().toString());
        intent.putExtra("DATE_LIEU_ID", dateLieuId);
        intent.putExtra("NB_PLACES", Integer.parseInt(etNbPlaces.getText().toString()));
        startActivity(intent);
    }
}
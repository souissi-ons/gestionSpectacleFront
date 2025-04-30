package com.example.spectacleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleapp.R;
import com.example.spectacleapp.utils.AuthHelper;

public class ReservationLoggedInActivity extends AppCompatActivity {

    private EditText etNbPlaces;
    private Button btnReserver;
    private AuthHelper authHelper;
    private Long dateLieuId;
    private int placesDisponibles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_logged_in);

        authHelper = new AuthHelper(this);
        dateLieuId = getIntent().getLongExtra("DATE_LIEU_ID", -1);
        placesDisponibles = getIntent().getIntExtra("PLACES_DISPONIBLES", 0);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etNbPlaces = findViewById(R.id.et_nb_places);
        btnReserver = findViewById(R.id.btn_reserver);
    }

    private void setupListeners() {
        btnReserver.setOnClickListener(v -> {
            if (validateInput()) {
                proceedToPayment();
            }
        });
    }

    private boolean validateInput() {
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
            return true;
        } catch (NumberFormatException e) {
            etNbPlaces.setError("Veuillez entrer un nombre valide");
            return false;
        }
    }

    private void proceedToPayment() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("NB_PLACES", Integer.parseInt(etNbPlaces.getText().toString()));
        startActivity(intent);
    }
}
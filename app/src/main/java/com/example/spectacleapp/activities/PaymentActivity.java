package com.example.spectacleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleapp.R;
import com.example.spectacleapp.dtos.PaymentInfoDTO;
import com.example.spectacleapp.dtos.ReservationDTO;
import com.example.spectacleapp.model.Reservation;
import com.example.spectacleapp.service.ApiClient;
import com.example.spectacleapp.service.ReservationService;
import com.example.spectacleapp.utils.AuthHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private EditText etCardNumber, etCardHolder, etExpiry, etCvv;
    private Button btnPayer;
    private ProgressBar progressBar;
    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        authHelper = new AuthHelper(this);
        initViews();
        setupListeners();
    }

    private void initViews() {
        etCardNumber = findViewById(R.id.et_card_number);
        etCardHolder = findViewById(R.id.et_card_holder);
        etExpiry = findViewById(R.id.et_expiry);
        etCvv = findViewById(R.id.et_cvv);
        btnPayer = findViewById(R.id.btn_payer);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnPayer.setOnClickListener(v -> {
            if (validatePaymentInfo()) {
                createReservation();
            }
        });
    }

    private boolean validatePaymentInfo() {
        String cardNumber = etCardNumber.getText().toString().trim();
        String cardHolder = etCardHolder.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();
        String cvv = etCvv.getText().toString().trim();

        if (cardNumber.isEmpty() || cardNumber.length() < 16) {
            etCardNumber.setError("Numéro de carte invalide");
            return false;
        }

        if (cardHolder.isEmpty()) {
            etCardHolder.setError("Nom du titulaire requis");
            return false;
        }

        if (expiry.isEmpty() || !expiry.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            etExpiry.setError("Format MM/AA requis");
            return false;
        }

        if (cvv.isEmpty() || cvv.length() < 3) {
            etCvv.setError("Code CVV invalide");
            return false;
        }

        return true;
    }

    private void createReservation() {
        showLoading(true);
        ReservationDTO reservationDTO = buildReservationDTO();
        
        // Ajoutez ce log pour vérifier les données avant envoi
        Log.d("ReservationData", "DTO: " + reservationDTO.toString());
        Log.d("ReservationData", "DateLieuId: " + reservationDTO.getSpectacleDateLieuId());
        ReservationService service = ApiClient.getClient().create(ReservationService.class);
        Call<Reservation> call;


        if (authHelper.isUserLoggedIn()) {
            call = service.createReservation("Bearer " + authHelper.getAuthToken(), reservationDTO);
        } else {
            // Sans token - envoyer null
            call = service.createReservation(null, reservationDTO);
        }

        // 3. Exécuter l'appel
        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    showConfirmation(response.body());
                } else {
                    String errorMsg = "Erreur de paiement";
                    try {
                        errorMsg += ": " + response.errorBody().string();
                    } catch (IOException e) {
                        errorMsg += " (code: " + response.code() + ")";
                    }
                    showError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                showLoading(false);
                showError("Erreur réseau: " + t.getMessage());
                Log.e("PaymentError", "Échec du paiement", t);
            }
        });
    }

    private ReservationDTO buildReservationDTO() {
        ReservationDTO dto = new ReservationDTO();
        dto.setSpectacleDateLieuId(getIntent().getLongExtra("DATE_LIEU_ID", -1));
        dto.setNbPlaces(getIntent().getIntExtra("NB_PLACES", 1));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDate = sdf.format(new Date());
        dto.setDateReservation(currentDate);
        dto.setPaymentMethod("CARD");
        dto.setPaymentStatus("PAID");

        if (authHelper.isUserLoggedIn()) {
            dto.setUserId(authHelper.getUserId());
        } else {
            dto.setNom(getIntent().getStringExtra("NOM"));
            dto.setPrenom(getIntent().getStringExtra("PRENOM"));
            dto.setEmail(getIntent().getStringExtra("EMAIL"));
            dto.setTelephone(getIntent().getStringExtra("TELEPHONE"));

        }
        PaymentInfoDTO paymentInfo = new PaymentInfoDTO();
        paymentInfo.setCardNumber(etCardNumber.getText().toString());
        paymentInfo.setCardHolderName(etCardHolder.getText().toString());
        paymentInfo.setExpirationDate(etExpiry.getText().toString());
        paymentInfo.setCvv(etCvv.getText().toString());
        dto.setPaymentInfo(paymentInfo);

        Log.d("ReservationDebug", "Date envoyée: " + dto.getDateReservation());
        Log.d("ReservationDebug", "User logged in: " + authHelper.isUserLoggedIn());

        return dto;
    }

    private void showConfirmation(Reservation reservation) {
        if (reservation != null && reservation.getId() > 0) {

            Intent intent = new Intent(this, ConfirmationActivity.class);
            intent.putExtra("RESERVATION_ID", reservation.getId());
            startActivity(intent);
            finish();
        } else {
            // Ajout de logs détaillés
            Log.e("ReservationError", "Réponse invalide: " + (reservation == null ? "null" : reservation.toString()));
            showError("Échec de la réservation: " + (reservation == null ? "réponse vide" : "ID invalide"));
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnPayer.setEnabled(!show);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
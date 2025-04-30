package com.example.spectacleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleapp.MainActivity;
import com.example.spectacleapp.R;
import com.example.spectacleapp.model.LoginRequest;
import com.example.spectacleapp.model.LoginResponse;
import com.example.spectacleapp.service.ApiClient;
import com.example.spectacleapp.service.ApiService;
import com.example.spectacleapp.utils.AuthHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton;
    private View progressBar;
    private ApiService apiService;
    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des services
        apiService = ApiClient.getClient().create(ApiService.class);
        authHelper = new AuthHelper(this);

        // Initialisation des vues
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        // Si l'utilisateur est déjà connecté, rediriger
        if (authHelper.isUserLoggedIn()) {
            redirectToMainActivity();
            return;
        }

        // Gestion du clic sur le bouton de connexion
        loginButton.setOnClickListener(v -> attemptLogin());

        // Lien vers l'écran d'inscription
        findViewById(R.id.signupRedirect).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email requis");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Mot de passe requis");
            return;
        }

        showProgress(true);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                showProgress(false);

                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body());
                } else {
                    showError("Email ou mot de passe incorrect");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showProgress(false);
                showError("Erreur de connexion: " + t.getMessage());
            }
        });
    }

    private void handleLoginSuccess(LoginResponse response) {
        if (response.getId() == 0) {
            Toast.makeText(this, "Erreur serveur: ID utilisateur manquant", Toast.LENGTH_LONG).show();
            return;
        }

        authHelper.saveAuthData(
                response.getToken(),
                response.getEmail(),
                response.getId()
        );

        Log.d("AUTH_DEBUG", "ID sauvegardé: " + response.getId()); // Doit afficher un ID valide

        if (getIntent().getBooleanExtra("REDIRECT_TO_RESERVATION", false)) {
            redirectToReservationActivity();
        } else {
            // Redirection normale
            redirectToMainActivity();
        }
    }

    private void redirectToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void redirectToReservationActivity() {
        Intent intent = new Intent(this, ReservationLoggedInActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
        finish();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Réactiver le bouton au cas où il serait désactivé
        loginButton.setEnabled(true);
    }
}
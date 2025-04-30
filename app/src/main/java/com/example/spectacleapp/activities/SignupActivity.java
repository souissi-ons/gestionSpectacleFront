package com.example.spectacleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleapp.MainActivity;
import com.example.spectacleapp.R;
import com.example.spectacleapp.model.RegisterRequest;
import com.example.spectacleapp.model.RegisterResponse;
import com.example.spectacleapp.service.ApiClient;
import com.example.spectacleapp.service.ApiService;
import com.example.spectacleapp.utils.AuthHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout nameInput, firstnameInput, emailInput, passwordInput, confirmPasswordInput;
    private TextInputEditText nameEditText, firstnameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private MaterialButton signupButton;
    private View progressBar;

    private ApiService apiService;
    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authHelper = new AuthHelper(this);
        apiService = ApiClient.getClient().create(ApiService.class);

        initViews();
        setupListeners();
    }

    private void initViews() {
        nameInput = findViewById(R.id.nameInput);
        firstnameInput = findViewById(R.id.firstnameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        nameEditText = findViewById(R.id.nameEditText);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        signupButton = findViewById(R.id.signupButton);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        signupButton.setOnClickListener(v -> {
            if (validateInputs()) {
                registerUser();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validation du nom
        if (nameEditText.getText().toString().trim().isEmpty()) {
            nameInput.setError("Le nom est obligatoire");
            isValid = false;
        } else {
            nameInput.setError(null);
        }

        // Validation du prénom
        if (firstnameEditText.getText().toString().trim().isEmpty()) {
            firstnameInput.setError("Le prénom est obligatoire");
            isValid = false;
        } else {
            firstnameInput.setError(null);
        }

        // Validation de l'email
        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailInput.setError("L'email est obligatoire");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString().trim()).matches()) {
            emailInput.setError("Email invalide");
            isValid = false;
        } else {
            emailInput.setError(null);
        }

        // Validation du mot de passe
        if (password.isEmpty()) {
            passwordInput.setError("Le mot de passe est obligatoire");
            isValid = false;
        } else if (password.length() < 6) {
            passwordInput.setError("Le mot de passe doit contenir au moins 6 caractères");
            isValid = false;
        } else {
            passwordInput.setError(null);
        }

        // Validation de la confirmation
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Les mots de passe ne correspondent pas");
            isValid = false;
        } else {
            confirmPasswordInput.setError(null);
        }

        return isValid;
    }

    private void registerUser() {
        showLoading(true);

        RegisterRequest registerRequest = new RegisterRequest(
                nameEditText.getText().toString().trim(),
                firstnameEditText.getText().toString().trim(),
                emailEditText.getText().toString().trim(),
                passwordEditText.getText().toString().trim()
        );

        Call<RegisterResponse> call = apiService.registerUser(registerRequest);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    handleRegistrationSuccess(response.body(), registerRequest.getEmail());
                } else {
                    Toast.makeText(SignupActivity.this,
                            "Erreur d'inscription: " + response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                showLoading(false);
                Toast.makeText(SignupActivity.this,
                        "Erreur réseau: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleRegistrationSuccess(RegisterResponse response, String email) {
        if (response.getId() == 0) {
            Toast.makeText(this, "Erreur serveur: ID utilisateur manquant", Toast.LENGTH_LONG).show();
            return;
        }

        authHelper.saveAuthData(
                response.getToken(),
                email,
                response.getId()
        );

        Toast.makeText(this, "Inscription réussie!", Toast.LENGTH_SHORT).show();

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


    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        signupButton.setEnabled(!show);
    }
}
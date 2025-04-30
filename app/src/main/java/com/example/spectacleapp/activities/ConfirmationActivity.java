package com.example.spectacleapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleapp.MainActivity;
import com.example.spectacleapp.R;
import com.example.spectacleapp.utils.AuthHelper;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        TextView tvConfirmation = findViewById(R.id.tv_confirmation);
        Button btnHome = findViewById(R.id.btn_home);

        Long reservationId = getIntent().getLongExtra("RESERVATION_ID", -1);
        String message = String.format(getString(R.string.reservation_confirmation));
        tvConfirmation.setText(message);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

    }
}
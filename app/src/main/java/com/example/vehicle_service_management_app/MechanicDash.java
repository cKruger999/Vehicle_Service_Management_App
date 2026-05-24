package com.example.vehicle_service_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MechanicDash extends AppCompatActivity {

    TextView tvMechanicWelcome;
    Button btnCreateBooking, btnViewBookings, btnUpdateStatus, btnLogout;
    String mechanicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dash);

        mechanicName = getIntent().getStringExtra("MECHANIC_NAME");
        if (mechanicName == null || mechanicName.isEmpty()) {
            mechanicName = "Mechanic";
        }

        tvMechanicWelcome = findViewById(R.id.tvMechanicWelcome);
        btnCreateBooking  = findViewById(R.id.btnCreateBooking);
        btnViewBookings   = findViewById(R.id.btnViewBookings);
        btnUpdateStatus   = findViewById(R.id.btnUpdateStatus);
        btnLogout         = findViewById(R.id.btnLogout);

        tvMechanicWelcome.setText("Welcome, " + mechanicName);

        btnCreateBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MechanicDash.this, CreateBookingActivity.class);
                intent.putExtra("MECHANIC_NAME", mechanicName);
                startActivity(intent);
            }
        });

        btnViewBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MechanicDash.this, ViewBookingsActivity.class);
                intent.putExtra("MECHANIC_NAME", mechanicName);
                startActivity(intent);
            }
        });

        btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MechanicDash.this, UpdateServiceStatusActivity.class);
                intent.putExtra("MECHANIC_NAME", mechanicName);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MechanicDash.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
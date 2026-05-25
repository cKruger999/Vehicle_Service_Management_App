package com.example.vehicle_service_management_app.Mechanic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.Login;
import com.example.vehicle_service_management_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MechanicDash extends AppCompatActivity {

    TextView tvMechanicWelcome;
    Button btnLogout;
    String mechanicName, customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_dash);

        mechanicName = getIntent().getStringExtra("MECHANIC_NAME");
        if (mechanicName == null || mechanicName.isEmpty()) {
            mechanicName = "Mechanic";
        }

        tvMechanicWelcome = findViewById(R.id.tvMechanicWelcome);
        btnLogout         = findViewById(R.id.btnLogout);

        tvMechanicWelcome.setText("Welcome, " + mechanicName);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_mechanic_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_mechanic_home) {
                return true;

            } else if (id == R.id.nav_mechanic_create) {
                Intent intent = new Intent(getApplicationContext(), CreateBookingActivity.class);
                intent.putExtra("MECHANIC_NAME", mechanicName);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                return true;

            } else if (id == R.id.nav_mechanic_bookings) {
                Intent intent = new Intent(getApplicationContext(), ViewBookingsActivity.class);
                intent.putExtra("MECHANIC_NAME", mechanicName);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                return true;

            } else if (id == R.id.nav_mechanic_update) {
                Intent intent = new Intent(getApplicationContext(), UpdateServiceStatusActivity.class);
                intent.putExtra("MECHANIC_NAME", mechanicName);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            return false;
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
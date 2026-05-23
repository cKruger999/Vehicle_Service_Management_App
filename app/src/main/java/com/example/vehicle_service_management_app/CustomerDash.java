package com.example.vehicle_service_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerDash extends AppCompatActivity {

    String myCustomerId;
    TextView tvWelcome;
    Button btnVehicles, btnBookings, btnProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash);

        // Connect the XML
        tvWelcome = findViewById(R.id.tvCustomerWelcome);
        btnVehicles = findViewById(R.id.btnViewMyVehicles);
        btnBookings = findViewById(R.id.btnViewMyBookings);
        btnProfile = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Check if the Login screen sent the ID
        if (getIntent().hasExtra("LOGGED_IN_CUSTOMER_ID")) {
            myCustomerId = getIntent().getStringExtra("LOGGED_IN_CUSTOMER_ID");
        } else {
            Toast.makeText(this, "Error: No ID received", Toast.LENGTH_SHORT).show();
        }

        // ==========================================
        // BUTTON CLICKS
        // ==========================================

        btnVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerDash.this, "Opening Vehicles...", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CustomerDash.this, CustomerViewVehicle.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", myCustomerId);
                startActivity(intent);

            }
        });

        btnBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Create a 'MyBookingsActivity'
                Toast.makeText(CustomerDash.this, "Opening Bookings...", Toast.LENGTH_SHORT).show();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerDash.this, "Opening Profile...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerDash.this, CustomerUpdateDetails.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", myCustomerId);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CustomerDash.this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerDash.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
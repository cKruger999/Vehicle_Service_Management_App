package com.example.vehicle_service_management_app.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.Login;
import com.example.vehicle_service_management_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerDash extends AppCompatActivity {

    String myCustomerId, username;
    TextView tvWelcome;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash);

        tvWelcome = findViewById(R.id.tvCustomerWelcome);
        btnLogout = findViewById(R.id.btnLogout);

        if (getIntent().hasExtra("LOGGED_IN_CUSTOMER_ID")) {
            myCustomerId = getIntent().getStringExtra("LOGGED_IN_CUSTOMER_ID");
        } else {
            Toast.makeText(this, "Error: No ID received", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true;

            } else if (id == R.id.nav_vehicles) {
                Intent intent = new Intent(CustomerDash.this, CustomerViewVehicle.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", myCustomerId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (id == R.id.nav_bookings) {
                Intent intent = new Intent(CustomerDash.this, ViewServiceBookings.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", myCustomerId);
                intent.putExtra("MECHANIC_NAME", username);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(CustomerDash.this, CustomerUpdateDetails.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", myCustomerId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
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
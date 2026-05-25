package com.example.vehicle_service_management_app.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.Login;
import com.example.vehicle_service_management_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDash extends AppCompatActivity {

    Button btnAddCustomer, btnAddMechanic, btnAddVehicle, btnLogout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        // Bind the Add and Logout buttons
        btnAddCustomer = findViewById(R.id.btnDashAddCustomer);
        btnAddMechanic = findViewById(R.id.btnDashAddMechanic);
        btnAddVehicle = findViewById(R.id.btnDashAddVehicle);
        btnLogout = findViewById(R.id.btnAdminLogout);

        // Add Button Click Listeners
        btnAddCustomer.setOnClickListener(v -> startActivity(new Intent(AdminDash.this, AddCustomerActivity.class)));
        btnAddMechanic.setOnClickListener(v -> startActivity(new Intent(AdminDash.this, AddMechanicActivity.class)));
        btnAddVehicle.setOnClickListener(v -> startActivity(new Intent(AdminDash.this, AddVehicleActivity.class)));

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDash.this, Login.class);
            startActivity(intent);
            finish();
        });

        // Setup Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Already on the Home screen, do nothing
                return true;
            } else if (itemId == R.id.nav_customers) {
                startActivity(new Intent(AdminDash.this, ViewRecordsActivity.class));
                overridePendingTransition(0, 0); // Removes slide animation for a true tab feel
                return true;
            } else if (itemId == R.id.nav_mechanics) {
                startActivity(new Intent(AdminDash.this, ViewMechanicsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_vehicles) {
                startActivity(new Intent(AdminDash.this, ViewVehiclesActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure "Home" is always highlighted when returning to this dashboard
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
}
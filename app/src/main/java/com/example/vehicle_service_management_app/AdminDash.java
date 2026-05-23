package com.example.vehicle_service_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDash extends AppCompatActivity {

    Button btnAddCustomer, btnViewCustomers;
    Button btnAddMechanic, btnViewMechanics;
    Button btnAddVehicle, btnViewVehicles;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        btnAddCustomer = findViewById(R.id.btnDashAddCustomer);
        btnViewCustomers = findViewById(R.id.btnDashViewCustomers);

        btnAddMechanic = findViewById(R.id.btnDashAddMechanic);
        btnViewMechanics = findViewById(R.id.btnDashViewMechanics);

        btnAddVehicle = findViewById(R.id.btnDashAddVehicle);
        btnViewVehicles = findViewById(R.id.btnDashViewVehicles);

        btnLogout = findViewById(R.id.btnAdminLogout);

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, AddCustomerActivity.class));
            }
        });
        btnViewCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, ViewRecordsActivity.class));
            }
        });

        btnAddMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, AddMechanicActivity.class));
            }
        });
        btnViewMechanics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, ViewMechanicsActivity.class));
            }
        });

        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, AddVehicleActivity.class));
            }
        });
        btnViewVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, ViewVehiclesActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDash.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
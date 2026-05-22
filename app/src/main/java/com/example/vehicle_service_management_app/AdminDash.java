package com.example.vehicle_service_management_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDash extends AppCompatActivity {

    Button btnAddCustomer, btnAddMechanic, btnAddVehicle, btnViewRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash);

        btnAddCustomer = findViewById(R.id.btnAddCustomer);
        btnAddMechanic = findViewById(R.id.btnAddMechanic);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        btnViewRecords = findViewById(R.id.btnViewRecords);

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, AddCustomerActivity.class));
            }
        });

        btnAddMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, AddMechanicActivity.class));
            }
        });

        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, AddVehicleActivity.class));
            }
        });

        btnViewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDash.this, ViewRecordsActivity.class));
            }
        });
    }
}
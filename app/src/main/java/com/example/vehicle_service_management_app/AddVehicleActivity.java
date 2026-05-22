package com.example.vehicle_service_management_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddVehicleActivity extends AppCompatActivity {

    EditText etBrand, etModel, etRegNumber, etYear, etCustomerId;
    Button btnSaveVehicle;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        myDb = new DatabaseHelper(this);

        etBrand = findViewById(R.id.etVehicleBrand);
        etModel = findViewById(R.id.etVehicleModel);
        etRegNumber = findViewById(R.id.etRegNumber);
        etYear = findViewById(R.id.etVehicleYear);
        etCustomerId = findViewById(R.id.etCustomerId);
        btnSaveVehicle = findViewById(R.id.btnSaveVehicle);

        btnSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = etBrand.getText().toString().trim();
                String model = etModel.getText().toString().trim();
                String reg = etRegNumber.getText().toString().trim();
                String year = etYear.getText().toString().trim();
                String custId = etCustomerId.getText().toString().trim();

                if (brand.isEmpty() || model.isEmpty() || reg.isEmpty() || year.isEmpty() || custId.isEmpty()) {
                    Toast.makeText(AddVehicleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = myDb.insertVehicle(brand, model, reg, year, custId);
                    if (isInserted) {
                        Toast.makeText(AddVehicleActivity.this, "Vehicle Saved!", Toast.LENGTH_SHORT).show();
                        etBrand.setText("");
                        etModel.setText("");
                        etRegNumber.setText("");
                        etYear.setText("");
                        etCustomerId.setText("");
                    } else {
                        Toast.makeText(AddVehicleActivity.this, "Error saving vehicle", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
package com.example.vehicle_service_management_app.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.DatabaseHelper;
import com.example.vehicle_service_management_app.R;

public class AddVehicleActivity extends AppCompatActivity {

    EditText etBrand, etModel, etRegNumber, etYear, etCustomerId;
    Spinner spnVehicleType;
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
        spnVehicleType = findViewById(R.id.spnVehicleType);
        btnSaveVehicle = findViewById(R.id.btnSaveVehicle);


        String[] vehicleTypes = {"Sedan", "Truck", "Motorcycle"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVehicleType.setAdapter(adapter);

        btnSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = etBrand.getText().toString().trim();
                String model = etModel.getText().toString().trim();
                String reg = etRegNumber.getText().toString().trim();
                String year = etYear.getText().toString().trim();
                String custId = etCustomerId.getText().toString().trim();
                String selectedType = spnVehicleType.getSelectedItem().toString();


                if (brand.isEmpty() || model.isEmpty() || reg.isEmpty() || year.isEmpty() || custId.isEmpty()) {
                    Toast.makeText(AddVehicleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {

                    boolean isInserted = myDb.insertVehicle(brand, model, reg, year, selectedType, custId);

                    if (isInserted) {
                        Toast.makeText(AddVehicleActivity.this, "Vehicle Saved!", Toast.LENGTH_SHORT).show();
                        etBrand.setText("");
                        etModel.setText("");
                        etRegNumber.setText("");
                        etYear.setText("");
                        etCustomerId.setText("");
                        spnVehicleType.setSelection(0);
                    } else {
                        Toast.makeText(AddVehicleActivity.this, "Error saving vehicle", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
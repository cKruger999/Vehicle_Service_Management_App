package com.example.vehicle_service_management_app.Admin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.DatabaseHelper;
import com.example.vehicle_service_management_app.R;

public class UpdateVehicleActivity extends AppCompatActivity {

    TextView tvId;
    EditText etBrand, etModel, etReg, etYear, etCustId;
    Button btnUpdate, btnDelete;
    DatabaseHelper myDb;
    String vehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_vehicles);

        myDb = new DatabaseHelper(this);

        tvId = findViewById(R.id.tvUpdVehicleId);
        etBrand = findViewById(R.id.etUpdVehBrand);
        etModel = findViewById(R.id.etUpdVehModel);
        etReg = findViewById(R.id.etUpdVehReg);
        etYear = findViewById(R.id.etUpdVehYear);
        etCustId = findViewById(R.id.etUpdVehCustId);
        btnUpdate = findViewById(R.id.btnUpdateVehicle);
        btnDelete = findViewById(R.id.btnDeleteVehicle);

        if (getIntent().hasExtra("VEHICLE_ID")) {
            vehicleId = getIntent().getStringExtra("VEHICLE_ID");

            Cursor cursor = myDb.getVehicleById(vehicleId);

            if (cursor.moveToFirst()) {
                tvId.setText("Vehicle ID: " + cursor.getString(0));
                etBrand.setText(cursor.getString(1));
                etModel.setText(cursor.getString(2));
                etReg.setText(cursor.getString(3));
                etYear.setText(cursor.getString(4));
                etCustId.setText(cursor.getString(5)); // The linked customer ID
            }
            cursor.close();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand = etBrand.getText().toString().trim();
                String model = etModel.getText().toString().trim();
                String reg = etReg.getText().toString().trim();
                String year = etYear.getText().toString().trim();
                String custId = etCustId.getText().toString().trim();

                if (brand.isEmpty() || model.isEmpty() || reg.isEmpty() || year.isEmpty() || custId.isEmpty()) {
                    Toast.makeText(UpdateVehicleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isUpdated = myDb.updateVehicle(vehicleId, brand, model, reg, year, custId);
                    if (isUpdated) {
                        Toast.makeText(UpdateVehicleActivity.this, "Vehicle Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateVehicleActivity.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDeleted = myDb.deleteVehicle(vehicleId);
                if (isDeleted) {
                    Toast.makeText(UpdateVehicleActivity.this, "Vehicle Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateVehicleActivity.this, "Error Deleting", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
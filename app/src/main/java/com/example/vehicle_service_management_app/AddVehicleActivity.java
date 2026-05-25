package com.example.vehicle_service_management_app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddVehicleActivity extends AppCompatActivity {

    EditText etBrand, etModel, etRegNumber, etYear;
    AutoCompleteTextView actvCustomer;
    Spinner spnVehicleType;
    Button btnSaveVehicle;
    DatabaseHelper myDb;
    List<String> customerIdList;
    List<String> customerLabelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        myDb = new DatabaseHelper(this);

        etBrand        = findViewById(R.id.etVehicleBrand);
        etModel        = findViewById(R.id.etVehicleModel);
        etRegNumber    = findViewById(R.id.etRegNumber);
        etYear         = findViewById(R.id.etVehicleYear);
        actvCustomer   = findViewById(R.id.actvCustomer);
        spnVehicleType = findViewById(R.id.spnVehicleType);
        btnSaveVehicle = findViewById(R.id.btnSaveVehicle);

        String[] vehicleTypes = {"Sedan", "Truck", "Motorcycle"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnVehicleType.setAdapter(typeAdapter);

        loadCustomersIntoDropdown();

        btnSaveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String brand        = etBrand.getText().toString().trim();
                String model        = etModel.getText().toString().trim();
                String reg          = etRegNumber.getText().toString().trim();
                String year         = etYear.getText().toString().trim();
                String customerText = actvCustomer.getText().toString().trim();
                String selectedType = spnVehicleType.getSelectedItem().toString();

                if (brand.isEmpty() || model.isEmpty() || reg.isEmpty() || year.isEmpty() || customerText.isEmpty()) {
                    Toast.makeText(AddVehicleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int customerIndex = customerLabelList.indexOf(customerText);

                if (customerIndex == -1) {
                    Toast.makeText(AddVehicleActivity.this, "Please select a valid customer from the list", Toast.LENGTH_SHORT).show();
                    return;
                }

                String customerId = customerIdList.get(customerIndex);
                boolean isInserted = myDb.insertVehicle(brand, model, reg, year, selectedType, customerId);

                if (isInserted) {
                    Toast.makeText(AddVehicleActivity.this, "Vehicle Saved!", Toast.LENGTH_SHORT).show();
                    etBrand.setText("");
                    etModel.setText("");
                    etRegNumber.setText("");
                    etYear.setText("");
                    actvCustomer.setText("");
                    spnVehicleType.setSelection(0);
                } else {
                    Toast.makeText(AddVehicleActivity.this, "Error saving vehicle", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadCustomersIntoDropdown() {
        customerIdList    = new ArrayList<>();
        customerLabelList = new ArrayList<>();

        Cursor cursor = myDb.getAllCustomers();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id      = cursor.getString(cursor.getColumnIndexOrThrow("customerID"));
                String name    = cursor.getString(cursor.getColumnIndexOrThrow("customerName"));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow("customerSurname"));
                customerIdList.add(id);
                customerLabelList.add(id + " - " + name + " " + surname);
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, customerLabelList);
        actvCustomer.setAdapter(adapter);

        actvCustomer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                actvCustomer.showDropDown();
            }
        });

        actvCustomer.setOnClickListener(v -> actvCustomer.showDropDown());
    }
}
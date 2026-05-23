package com.example.vehicle_service_management_app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateBookingActivity extends AppCompatActivity {

    EditText etServiceType, etBookingDate;
    Spinner spinnerVehicle;
    Button btnSaveBooking;
    DatabaseHelper myDb;
    String mechanicName;
    List<String> vehicleIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        myDb = new DatabaseHelper(this);
        mechanicName = getIntent().getStringExtra("MECHANIC_NAME");

        etServiceType = findViewById(R.id.etServiceType);
        etBookingDate = findViewById(R.id.etBookingDate);
        spinnerVehicle = findViewById(R.id.spinnerVehicle);
        btnSaveBooking = findViewById(R.id.btnSaveBooking);

        loadVehiclesIntoSpinner();

        btnSaveBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = etServiceType.getText().toString().trim();
                String bookingDate = etBookingDate.getText().toString().trim();

                if (serviceType.isEmpty() || bookingDate.isEmpty()) {
                    Toast.makeText(CreateBookingActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (vehicleIdList == null || vehicleIdList.isEmpty()) {
                    Toast.makeText(CreateBookingActivity.this, "No vehicles available in the system", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedIndex = spinnerVehicle.getSelectedItemPosition();
                String vehicleId = vehicleIdList.get(selectedIndex);

                boolean isInserted = myDb.insertBooking(serviceType, bookingDate, "Pending", mechanicName, vehicleId);

                if (isInserted) {
                    Toast.makeText(CreateBookingActivity.this, "Booking Created!", Toast.LENGTH_SHORT).show();
                    etServiceType.setText("");
                    etBookingDate.setText("");
                } else {
                    Toast.makeText(CreateBookingActivity.this, "Error creating booking", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadVehiclesIntoSpinner() {
        vehicleIdList = new ArrayList<>();
        List<String> vehicleLabels = new ArrayList<>();

        Cursor cursor = myDb.getAllVehicles();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("vehicleID"));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow("vehicleBrand"));
                String model = cursor.getString(cursor.getColumnIndexOrThrow("vehicleModel"));
                String reg = cursor.getString(cursor.getColumnIndexOrThrow("registrationNumber"));
                vehicleIdList.add(id);
                vehicleLabels.add(brand + " " + model + " (" + reg + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicle.setAdapter(adapter);
    }
}
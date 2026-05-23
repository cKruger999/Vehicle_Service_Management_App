package com.example.vehicle_service_management_app;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateBookingActivity extends AppCompatActivity {

    EditText etServiceType, etBookingDate;
    AutoCompleteTextView actvVehicle;
    Button btnSaveBooking;
    DatabaseHelper myDb;
    String mechanicName;
    List<String> vehicleIdList;
    List<String> vehicleLabelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);

        myDb = new DatabaseHelper(this);
        mechanicName = getIntent().getStringExtra("MECHANIC_NAME");

        etServiceType = findViewById(R.id.etServiceType);
        etBookingDate = findViewById(R.id.etBookingDate);
        actvVehicle = findViewById(R.id.actvVehicle);
        btnSaveBooking = findViewById(R.id.btnSaveBooking);

        setupDatePicker();
        loadVehiclesIntoDropdown();

        btnSaveBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = etServiceType.getText().toString().trim();
                String bookingDate = etBookingDate.getText().toString().trim();
                String vehicleText = actvVehicle.getText().toString().trim();

                if (vehicleText.isEmpty() || serviceType.isEmpty() || bookingDate.isEmpty()) {
                    Toast.makeText(CreateBookingActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                int vehicleIndex = vehicleLabelList.indexOf(vehicleText);

                if (vehicleIndex == -1) {
                    Toast.makeText(CreateBookingActivity.this, "Please select a valid vehicle from the list", Toast.LENGTH_SHORT).show();
                    return;
                }

                String vehicleId = vehicleIdList.get(vehicleIndex);
                boolean isInserted = myDb.insertBooking(serviceType, bookingDate, "Pending", mechanicName, vehicleId);

                if (isInserted) {
                    Toast.makeText(CreateBookingActivity.this, "Booking Created!", Toast.LENGTH_SHORT).show();
                    etServiceType.setText("");
                    etBookingDate.setText("");
                    actvVehicle.setText("");
                } else {
                    Toast.makeText(CreateBookingActivity.this, "Error creating booking", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupDatePicker() {
        etBookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateBookingActivity.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                            etBookingDate.setText(formattedDate);
                        },
                        year, month, day
                );
                dialog.show();
            }
        });
    }

    private void loadVehiclesIntoDropdown() {
        vehicleIdList = new ArrayList<>();
        vehicleLabelList = new ArrayList<>();

        Cursor cursor = myDb.getAllVehicles();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("vehicleID"));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow("vehicleBrand"));
                String model = cursor.getString(cursor.getColumnIndexOrThrow("vehicleModel"));
                String reg = cursor.getString(cursor.getColumnIndexOrThrow("registrationNumber"));
                vehicleIdList.add(id);
                vehicleLabelList.add(brand + " " + model + " (" + reg + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, vehicleLabelList);
        actvVehicle.setAdapter(adapter);

        actvVehicle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                actvVehicle.showDropDown();
            }
        });

        actvVehicle.setOnClickListener(v -> actvVehicle.showDropDown());
    }
}
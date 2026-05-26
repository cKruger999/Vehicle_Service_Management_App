package com.example.vehicle_service_management_app.Customer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.DatabaseHelper;
import com.example.vehicle_service_management_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewServiceBookings extends AppCompatActivity {

    ListView lvGarage;
    DatabaseHelper myDb;
    String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service_bookings);


        myDb = new DatabaseHelper(this);
        lvGarage = findViewById(R.id.lvCustomersGarage);

        customerId = getIntent().getStringExtra("LOGGED_IN_CUSTOMER_ID");

        if (customerId == null || customerId.isEmpty()) {
            Toast.makeText(this, "Session Error", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayGarageList();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_bookings);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(ViewServiceBookings.this, CustomerDash.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", customerId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (id == R.id.nav_vehicles) {
                Intent intent = new Intent(ViewServiceBookings.this, CustomerViewVehicle.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", customerId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (id == R.id.nav_bookings) {
                return true;

            } else if (id == R.id.nav_profile) {
                Intent intent = new Intent(ViewServiceBookings.this, CustomerUpdateDetails.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", customerId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    private void displayGarageList() {
        Cursor cursor = myDb.getCustomerDashboardData(customerId);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No vehicles linked to your profile.", Toast.LENGTH_LONG).show();
            return;
        }

        CursorAdapter adapter = new CursorAdapter(this, cursor, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.vehicle_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                ImageView imgIcon = view.findViewById(R.id.imgVehicleIcon);
                TextView tvDetails = view.findViewById(R.id.tvVehicleDetails);
                TextView tvStatus = view.findViewById(R.id.tvVehicleStatus);

                String brand = cursor.getString(cursor.getColumnIndexOrThrow("vehicleBrand"));
                String model = cursor.getString(cursor.getColumnIndexOrThrow("vehicleModel"));
                String reg = cursor.getString(cursor.getColumnIndexOrThrow("registrationNumber"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("vehicleType"));

                int statusIdx = cursor.getColumnIndex("serviceStatus");
                String status = "No Booking";
                if (statusIdx != -1 && cursor.getString(statusIdx) != null) {
                    status = cursor.getString(statusIdx);
                }

                int dateIdx = cursor.getColumnIndex("bookingDate");
                String date = "N/A";
                if (dateIdx != -1 && cursor.getString(dateIdx) != null) {
                    date = cursor.getString(dateIdx);
                }

                int mechIdx = cursor.getColumnIndex("assignedMechanic");
                String mechanic = "Unassigned";
                if (mechIdx != -1 && cursor.getString(mechIdx) != null) {
                    mechanic = cursor.getString(mechIdx);
                }

                String fullDetails = brand + " " + model + "\nType: " + type + " | Reg: " + reg + "\nDate: " + date + " | Mechanic: " + mechanic;

                tvDetails.setText(fullDetails);
                tvStatus.setText("Status: " + status);

                if (type != null) {
                    if (type.equalsIgnoreCase("Truck")) {
                        imgIcon.setImageResource(R.drawable.ic_truck);
                    } else if (type.equalsIgnoreCase("Motorcycle") || type.equalsIgnoreCase("Bike")) {
                        imgIcon.setImageResource(R.drawable.ic_motorcycle);
                    } else {
                        imgIcon.setImageResource(R.drawable.ic_sedan);
                    }
                } else {
                    imgIcon.setImageResource(R.drawable.ic_sedan);
                }
            }
        };

        lvGarage.setAdapter(adapter);
    }
}
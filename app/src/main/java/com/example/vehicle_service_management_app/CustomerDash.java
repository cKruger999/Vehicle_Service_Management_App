package com.example.vehicle_service_management_app;

import android.content.Context;
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

public class CustomerDash extends AppCompatActivity {

    ListView lvGarage;
    DatabaseHelper myDb;
    String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash);


        myDb = new DatabaseHelper(this);
        lvGarage = findViewById(R.id.lvCustomersGarage);

        // Retrieve the logged-in customer's ID from the Login intent bundle
        customerId = getIntent().getStringExtra("LOGGED_IN_CUSTOMER_ID");

        if (customerId == null || customerId.isEmpty()) {
            Toast.makeText(this, "Session Error", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayGarageList();
    }

    private void displayGarageList() {
        Cursor cursor = myDb.getCustomerDashboardData(customerId);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No vehicles linked to your profile.", Toast.LENGTH_LONG).show();
            return;
        }

        // Use a standard CursorAdapter. It is much simpler and more direct.
        CursorAdapter adapter = new CursorAdapter(this, cursor, 0) {

            // 1. Inflate the layout for each row
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.vehicle_list_item, parent, false);
            }

            // 2. Bind the data from the database to your layout views
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Find your views
                ImageView imgIcon = view.findViewById(R.id.imgVehicleIcon);
                TextView tvDetails = view.findViewById(R.id.tvVehicleDetails);
                TextView tvStatus = view.findViewById(R.id.tvVehicleStatus);

                // Get data from database
                String brand = cursor.getString(cursor.getColumnIndexOrThrow("vehicleBrand"));
                String model = cursor.getString(cursor.getColumnIndexOrThrow("vehicleModel"));
                String reg = cursor.getString(cursor.getColumnIndexOrThrow("registrationNumber"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("vehicleType"));

                int statusIdx = cursor.getColumnIndex("serviceStatus");
                String status = (statusIdx != -1) ? cursor.getString(statusIdx) : "Pending";
                if (status == null || status.isEmpty()) status = "Pending";

                // Set Text
                tvDetails.setText(brand + " " + model + "\nType: " + type + " | Reg: " + reg);
                tvStatus.setText("Status: " + status);

                // Set Image
                if (type != null) {
                    if (type.equalsIgnoreCase("Truck")) {
                        imgIcon.setImageResource(R.drawable.ic_truck);
                    } else if (type.equalsIgnoreCase("Motorcycle") || type.equalsIgnoreCase("Bike")) {
                        imgIcon.setImageResource(R.drawable.ic_motorcycle);
                    } else {
                        // Default to Sedan for anything else
                        imgIcon.setImageResource(R.drawable.ic_sedan);
                    }
                } else {
                    imgIcon.setImageResource(R.drawable.ic_sedan);
                }
            }
        };

        // Bind the data to your layout screen
        lvGarage.setAdapter(adapter);
    }
}
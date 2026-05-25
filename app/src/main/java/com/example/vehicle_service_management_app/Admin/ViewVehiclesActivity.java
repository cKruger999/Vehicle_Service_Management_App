package com.example.vehicle_service_management_app.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.DatabaseHelper;
import com.example.vehicle_service_management_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class ViewVehiclesActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView lvVehicles;
    ArrayList<String> vehicleList;
    ArrayAdapter<String> adapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vehicles);

        myDb = new DatabaseHelper(this);
        lvVehicles = findViewById(R.id.lvVehicles);
        vehicleList = new ArrayList<>();

        loadData();

        lvVehicles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = vehicleList.get(position);
                String[] parts = selectedItem.split(" - ");
                String vehicleId = parts[0].replace("ID: ", "");

                Intent intent = new Intent(ViewVehiclesActivity.this, UpdateVehicleActivity.class);
                intent.putExtra("VEHICLE_ID", vehicleId);
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_vehicles);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ViewVehiclesActivity.this, AdminDash.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_customers) {
                startActivity(new Intent(ViewVehiclesActivity.this, ViewRecordsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_mechanics) {
                startActivity(new Intent(ViewVehiclesActivity.this, ViewMechanicsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_vehicles) {
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_vehicles);
        }
    }

    private void loadData() {
        vehicleList.clear();
        Cursor cursor = myDb.getAllVehicles();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No vehicles found.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String details = "ID: " + cursor.getString(0) + " - " + cursor.getString(1) + " " + cursor.getString(2) + " (" + cursor.getString(3) + ")";
                vehicleList.add(details);
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehicleList);
        lvVehicles.setAdapter(adapter);
    }
}
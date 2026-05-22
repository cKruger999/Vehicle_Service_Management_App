package com.example.vehicle_service_management_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ViewVehiclesActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView lvVehicles;
    ArrayList<String> vehicleList;
    ArrayAdapter<String> adapter;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
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
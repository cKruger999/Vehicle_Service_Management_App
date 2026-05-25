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

public class ViewMechanicsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView lvMechanics;
    ArrayList<String> mechanicList;
    ArrayAdapter<String> adapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mechanics);

        myDb = new DatabaseHelper(this);
        lvMechanics = findViewById(R.id.lvMechanics);
        mechanicList = new ArrayList<>();

        loadData();

        lvMechanics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = mechanicList.get(position);
                String[] parts = selectedItem.split(" - ");
                String mechanicId = parts[0].replace("ID: ", "");

                Intent intent = new Intent(ViewMechanicsActivity.this, UpdateMechanicActivity.class);
                intent.putExtra("MECHANIC_ID", mechanicId);
                startActivity(intent);
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_mechanics);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ViewMechanicsActivity.this, AdminDash.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_customers) {
                startActivity(new Intent(ViewMechanicsActivity.this, ViewRecordsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_mechanics) {
                return true;
            } else if (itemId == R.id.nav_vehicles) {
                startActivity(new Intent(ViewMechanicsActivity.this, ViewVehiclesActivity.class));
                overridePendingTransition(0, 0);
                finish();
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
            bottomNavigationView.setSelectedItemId(R.id.nav_mechanics);
        }
    }

    private void loadData() {
        mechanicList.clear();
        Cursor cursor = myDb.getAllMechanics();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No mechanics found.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String details = "ID: " + cursor.getString(0) + " - " + cursor.getString(1) + " (" + cursor.getString(2) + ")";
                mechanicList.add(details);
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mechanicList);
        lvMechanics.setAdapter(adapter);
    }
}
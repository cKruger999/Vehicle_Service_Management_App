package com.example.vehicle_service_management_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ViewMechanicsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView lvMechanics;
    ArrayList<String> mechanicList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We can reuse the same layout!
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
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
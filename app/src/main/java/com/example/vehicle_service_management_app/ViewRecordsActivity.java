package com.example.vehicle_service_management_app;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ViewRecordsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView lvCustomers;
    ArrayList<String> customerList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        myDb = new DatabaseHelper(this);
        lvCustomers = findViewById(R.id.lvCustomers);
        customerList = new ArrayList<>();

        Cursor cursor = myDb.getAllCustomers();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No customers found.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String details = "ID: " + cursor.getString(0) + " - " + cursor.getString(1) + " " + cursor.getString(2);
                customerList.add(details);
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerList);
        lvCustomers.setAdapter(adapter);
    }
}
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

        loadData();

        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 1. Get the exact text the user clicked on
                String selectedItem = customerList.get(position);

                String customerId = selectedItem.split(":")[0];


                Intent intent = new Intent(ViewRecordsActivity.this, UpdateCustomerActivity.class);
                intent.putExtra("CUSTOMER_ID", customerId); // "CUSTOMER_ID" is our secret password here
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
        customerList.clear();
        Cursor cursor = myDb.getAllCustomers();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No customers found.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {

                String details = cursor.getString(0) + ": " + cursor.getString(1) + " " + cursor.getString(2);
                customerList.add(details);
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, customerList);
        lvCustomers.setAdapter(adapter);
    }
}
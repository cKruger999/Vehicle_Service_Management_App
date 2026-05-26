package com.example.vehicle_service_management_app.Customer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.DatabaseHelper;
import com.example.vehicle_service_management_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerUpdateDetails extends AppCompatActivity {

    TextView tvId;
    EditText etPhone, etEmail;
    Button btnUpdate;
    DatabaseHelper myDb;
    String customerId, username;
    String currentName = "";
    String currentSurname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_update_details);

        myDb = new DatabaseHelper(this);

        tvId = findViewById(R.id.CustomerId);
        etPhone = findViewById(R.id.PhoneNumber);
        etEmail = findViewById(R.id.Email);
        btnUpdate = findViewById(R.id.btnUpdate);

        if (getIntent().hasExtra("LOGGED_IN_CUSTOMER_ID")) {
            customerId = getIntent().getStringExtra("LOGGED_IN_CUSTOMER_ID");

            Cursor cursor = myDb.getCustomerById(customerId);

            if (cursor != null && cursor.moveToFirst()) {
                tvId.setText("Customer ID: " + cursor.getString(0));

                currentName = cursor.getString(1);
                currentSurname = cursor.getString(2);

                etPhone.setText(cursor.getString(3));
                etEmail.setText(cursor.getString(4));
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                if (phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(CustomerUpdateDetails.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isUpdated = myDb.updateCustomer(customerId, currentName, currentSurname, phone, email);

                    if (isUpdated) {
                        Toast.makeText(CustomerUpdateDetails.this, "Customer Updated Successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CustomerUpdateDetails.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(CustomerUpdateDetails.this, CustomerDash.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", customerId);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.nav_vehicles) {
                Intent intent = new Intent(CustomerUpdateDetails.this, CustomerViewVehicle.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", customerId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (id == R.id.nav_bookings) {
                Intent intent = new Intent(CustomerUpdateDetails.this, ViewServiceBookings.class);
                intent.putExtra("LOGGED_IN_CUSTOMER_ID", customerId);
                intent.putExtra("MECHANIC_NAME", username);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else return id == R.id.nav_profile;
        });
    }
}
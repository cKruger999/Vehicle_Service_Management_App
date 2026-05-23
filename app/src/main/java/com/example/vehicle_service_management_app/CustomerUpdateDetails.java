package com.example.vehicle_service_management_app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CustomerUpdateDetails extends AppCompatActivity {

    TextView tvId;
    EditText etPhone, etEmail;
    Button btnUpdate;
    DatabaseHelper myDb;
    String customerId;
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

        if (getIntent().hasExtra("CUSTOMER_ID")) {
            customerId = getIntent().getStringExtra("CUSTOMER_ID");

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
                        finish();
                    } else {
                        Toast.makeText(CustomerUpdateDetails.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
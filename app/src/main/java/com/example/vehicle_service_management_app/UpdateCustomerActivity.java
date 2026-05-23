package com.example.vehicle_service_management_app;

import android.database.Cursor;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateCustomerActivity extends AppCompatActivity {

    TextView tvId;
    EditText etName, etSurname, etPhone, etEmail;

    Button btnUpdate, btnDelete;

    DatabaseHelper myDb;

    String customerId;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_customer);


        myDb = new DatabaseHelper(this);


        tvId = findViewById(R.id.tvUpdCustomerId);

        etName = findViewById(R.id.etUpdCustomerName);

        etSurname = findViewById(R.id.etUpdCustomerSurname);

        etPhone = findViewById(R.id.etUpdPhoneNumber);

        etEmail = findViewById(R.id.etUpdEmail);

        btnUpdate = findViewById(R.id.btnUpdateCustomer);

        btnDelete = findViewById(R.id.btnDeleteCustomer);


        if (getIntent().hasExtra("CUSTOMER_ID")) {

            customerId = getIntent().getStringExtra("CUSTOMER_ID");


            Cursor cursor = myDb.getCustomerById(customerId);


            if (cursor.moveToFirst()) {

                tvId.setText("Customer ID: " + cursor.getString(0));

                etName.setText(cursor.getString(1));

                etSurname.setText(cursor.getString(2));

                etPhone.setText(cursor.getString(3));

                etEmail.setText(cursor.getString(4));

            }

            cursor.close();

        }


        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                String name = etName.getText().toString().trim();

                String surname = etSurname.getText().toString().trim();

                String phone = etPhone.getText().toString().trim();

                String email = etEmail.getText().toString().trim();


                if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || email.isEmpty()) {

                    Toast.makeText(UpdateCustomerActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();

                } else {

                    boolean isUpdated = myDb.updateCustomer(customerId, name, surname, phone, email);

                    if (isUpdated) {

                        Toast.makeText(UpdateCustomerActivity.this, "Customer Updated Successfully!", Toast.LENGTH_SHORT).show();

                        finish();

                    } else {

                        Toast.makeText(UpdateCustomerActivity.this, "Error Updating", Toast.LENGTH_SHORT).show();

                    }

                }

            }

        });


        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                boolean isDeleted = myDb.deleteCustomer(customerId);

                if (isDeleted) {

                    Toast.makeText(UpdateCustomerActivity.this, "Customer Deleted Successfully!", Toast.LENGTH_SHORT).show();

                    finish();

                } else {

                    Toast.makeText(UpdateCustomerActivity.this, "Error Deleting", Toast.LENGTH_SHORT).show();

                }

            }

        });

    }

}


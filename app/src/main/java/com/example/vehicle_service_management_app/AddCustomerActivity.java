package com.example.vehicle_service_management_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddCustomerActivity extends AppCompatActivity {

    EditText etCustomerName, etCustomerSurname, etPhoneNumber, etEmail, etPassword;
    Button btnSaveCustomer;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        myDb = new DatabaseHelper(this);

        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerSurname = findViewById(R.id.etCustomerSurname);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etCustomerPassword);
        btnSaveCustomer = findViewById(R.id.btnSaveCustomer);

        btnSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etCustomerName.getText().toString().trim();
                String surname = etCustomerSurname.getText().toString().trim();
                String phone = etPhoneNumber.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AddCustomerActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = myDb.insertCustomer(name, surname, phone, email, password);
                    if (isInserted == true) {
                        Toast.makeText(AddCustomerActivity.this, "Customer Saved!", Toast.LENGTH_SHORT).show();
                        etCustomerName.setText("");
                        etCustomerSurname.setText("");
                        etPhoneNumber.setText("");
                        etEmail.setText("");
                        etPassword.setText("");
                    } else {
                        Toast.makeText(AddCustomerActivity.this, "Error saving customer", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
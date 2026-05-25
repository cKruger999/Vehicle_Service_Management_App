package com.example.vehicle_service_management_app.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle_service_management_app.DatabaseHelper;
import com.example.vehicle_service_management_app.R;

public class AddMechanicActivity extends AppCompatActivity {

    EditText etMechanicName, etSpecialization, etExperience, etPassword;
    Button btnSaveMechanic;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mechanic);

        myDb = new DatabaseHelper(this);

        etMechanicName = findViewById(R.id.etMechanicName);
        etSpecialization = findViewById(R.id.etSpecialization);
        etExperience = findViewById(R.id.etExperience);
        etPassword = findViewById(R.id.etMechanicPassword);
        btnSaveMechanic = findViewById(R.id.btnSaveMechanic);

        btnSaveMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etMechanicName.getText().toString().trim();
                String spec = etSpecialization.getText().toString().trim();
                String exp = etExperience.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (name.isEmpty() || spec.isEmpty() || exp.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AddMechanicActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = myDb.insertMechanic(name, spec, exp, password);
                    if (isInserted) {
                        Toast.makeText(AddMechanicActivity.this, "Mechanic Saved!", Toast.LENGTH_SHORT).show();
                        etMechanicName.setText("");
                        etSpecialization.setText("");
                        etExperience.setText("");
                        etPassword.setText("");
                    } else {
                        Toast.makeText(AddMechanicActivity.this, "Error saving mechanic", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
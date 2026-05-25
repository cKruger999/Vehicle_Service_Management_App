package com.example.vehicle_service_management_app.Admin;

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

public class UpdateMechanicActivity extends AppCompatActivity {

    TextView tvId;
    EditText etName, etSpec, etExp;
    Button btnUpdate, btnDelete;
    DatabaseHelper myDb;
    String mechanicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mechanic);

        myDb = new DatabaseHelper(this);

        tvId = findViewById(R.id.tvUpdMechanicId);
        etName = findViewById(R.id.etUpdMechName);
        etSpec = findViewById(R.id.etUpdMechSpec);
        etExp = findViewById(R.id.etUpdMechExp);
        btnUpdate = findViewById(R.id.btnUpdateMechanic);
        btnDelete = findViewById(R.id.btnDeleteMechanic);

        // ====================================================================
        // AUTO-POPULATE LOGIC
        // ====================================================================
        if (getIntent().hasExtra("MECHANIC_ID")) {
            mechanicId = getIntent().getStringExtra("MECHANIC_ID");

            // Ask the database for this specific mechanic
            Cursor cursor = myDb.getMechanicById(mechanicId);

            // Type the data into the screen boxes
            if (cursor.moveToFirst()) {
                tvId.setText("Mechanic ID: " + cursor.getString(0));
                etName.setText(cursor.getString(1));
                etSpec.setText(cursor.getString(2));
                etExp.setText(cursor.getString(3));
            }
            cursor.close();
        }
        // ====================================================================

        // The Update Button Logic
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String spec = etSpec.getText().toString().trim();
                String exp = etExp.getText().toString().trim();

                if (name.isEmpty() || spec.isEmpty() || exp.isEmpty()) {
                    Toast.makeText(UpdateMechanicActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isUpdated = myDb.updateMechanic(mechanicId, name, spec, exp);
                    if (isUpdated) {
                        Toast.makeText(UpdateMechanicActivity.this, "Mechanic Updated Successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Closes the screen and returns to the list
                    } else {
                        Toast.makeText(UpdateMechanicActivity.this, "Error Updating", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // The Delete Button Logic
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isDeleted = myDb.deleteMechanic(mechanicId);
                if (isDeleted) {
                    Toast.makeText(UpdateMechanicActivity.this, "Mechanic Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Closes the screen and returns to the list
                } else {
                    Toast.makeText(UpdateMechanicActivity.this, "Error Deleting", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
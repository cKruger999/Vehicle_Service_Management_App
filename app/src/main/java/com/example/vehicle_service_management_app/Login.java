package com.example.vehicle_service_management_app;

import android.annotation.SuppressLint;
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

public class Login extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.etUsername);
        editPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userRole = dbHelper.authenticateUser(username, password);

                if (userRole != null) {
                    Toast.makeText(Login.this, "Welcome " + username + " (" + userRole + ")", Toast.LENGTH_SHORT).show();
                    String profileId = dbHelper.getRoleBasedId(username, userRole);
                    Intent intent = null;

                    switch (userRole) {
                        case "Admin":
                            intent = new Intent(Login.this, AdminDash.class);
                            break;
                        case "Mechanic":
                            intent = new Intent(Login.this, MechanicDash.class);
                            intent.putExtra("MECHANIC_ID", profileId); // Dynamic fallback for mechanic tracking later
                            break;
                        case "Customer":
                            intent = new Intent(Login.this, CustomerDash.class);
                            intent.putExtra("CUSTOMER_ID", profileId); // Pass dynamic ID straight to customer garage view
                            break;
                    }

                    if (intent != null) {
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(Login.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
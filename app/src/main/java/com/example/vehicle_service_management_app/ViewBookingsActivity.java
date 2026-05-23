package com.example.vehicle_service_management_app;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewBookingsActivity extends AppCompatActivity {

    ListView lvBookings;
    DatabaseHelper myDb;
    String mechanicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        myDb = new DatabaseHelper(this);
        mechanicName = getIntent().getStringExtra("MECHANIC_NAME");
        lvBookings = findViewById(R.id.lvBookings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        Cursor cursor = myDb.getBookingsByMechanic(mechanicName);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No bookings assigned to you.", Toast.LENGTH_LONG).show();
            return;
        }

        CursorAdapter adapter = new CursorAdapter(this, cursor, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.booking_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tvBookingDetails = view.findViewById(R.id.tvBookingDetails);
                TextView tvBookingStatus = view.findViewById(R.id.tvBookingStatus);

                String bookingId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String serviceType = cursor.getString(cursor.getColumnIndexOrThrow("serviceType"));
                String bookingDate = cursor.getString(cursor.getColumnIndexOrThrow("bookingDate"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("serviceStatus"));

                int regIdx = cursor.getColumnIndex("registrationNumber");
                String reg = (regIdx != -1 && !cursor.isNull(regIdx)) ? cursor.getString(regIdx) : "N/A";

                tvBookingDetails.setText("Booking #" + bookingId + " | " + serviceType + "\nVehicle Reg: " + reg + "  |  Date: " + bookingDate);
                tvBookingStatus.setText("Status: " + status);
            }
        };

        lvBookings.setAdapter(adapter);
    }
}
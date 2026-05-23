package com.example.vehicle_service_management_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateServiceStatusActivity extends AppCompatActivity {

    ListView lvUpdateBookings;
    DatabaseHelper myDb;
    String mechanicName;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service_status);

        myDb = new DatabaseHelper(this);
        mechanicName = getIntent().getStringExtra("MECHANIC_NAME");
        lvUpdateBookings = findViewById(R.id.lvUpdateBookings);

        loadBookings();

        lvUpdateBookings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String bookingId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String serviceType = cursor.getString(cursor.getColumnIndexOrThrow("serviceType"));
                showStatusDialog(bookingId, serviceType);
            }
        });
    }

    private void loadBookings() {
        cursor = myDb.getBookingsByMechanic(mechanicName);

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
                tvBookingStatus.setText("Status: " + status + "  (Tap to update)");
            }
        };

        lvUpdateBookings.setAdapter(adapter);
    }

    private void showStatusDialog(String bookingId, String serviceType) {
        final String[] statusOptions = {"Pending", "In Progress", "Completed"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update status for: " + serviceType);
        builder.setItems(statusOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newStatus = statusOptions[which];
                boolean updated = myDb.updateBookingStatus(bookingId, newStatus);
                if (updated) {
                    Toast.makeText(UpdateServiceStatusActivity.this, "Status updated to: " + newStatus, Toast.LENGTH_SHORT).show();
                    loadBookings();
                } else {
                    Toast.makeText(UpdateServiceStatusActivity.this, "Error updating status", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }
}
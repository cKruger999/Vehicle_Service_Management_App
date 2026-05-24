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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class UpdateServiceStatusActivity extends AppCompatActivity {

    ListView lvUpdateBookings;
    TabLayout tabLayoutUpdateStatus;
    DatabaseHelper myDb;
    String mechanicName;
    Cursor activeCursor;

    private static final String[] TAB_FILTERS = {"In Progress", "Pending", "Completed", "All"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service_status);

        myDb = new DatabaseHelper(this);
        mechanicName = getIntent().getStringExtra("MECHANIC_NAME");
        lvUpdateBookings = findViewById(R.id.lvUpdateBookings);
        tabLayoutUpdateStatus = findViewById(R.id.tabLayoutUpdateStatus);

        for (String filter : TAB_FILTERS) {
            tabLayoutUpdateStatus.addTab(tabLayoutUpdateStatus.newTab().setText(filter));
        }

        tabLayoutUpdateStatus.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadBookings(TAB_FILTERS[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        loadBookings(TAB_FILTERS[0]);

        lvUpdateBookings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (activeCursor == null || !activeCursor.moveToPosition(position)) return;
                String bookingId   = activeCursor.getString(activeCursor.getColumnIndexOrThrow("_id"));
                String serviceType = activeCursor.getString(activeCursor.getColumnIndexOrThrow("serviceType"));
                showStatusDialog(bookingId, serviceType);
            }
        });
    }

    private void loadBookings(String filter) {
        activeCursor = filter.equals("All")
                ? myDb.getBookingsByMechanic(mechanicName)
                : myDb.getBookingsByMechanicAndStatus(mechanicName, filter);

        if (activeCursor == null || activeCursor.getCount() == 0) {
            lvUpdateBookings.setAdapter(null);
            Toast.makeText(this, "No \"" + filter + "\" bookings found.", Toast.LENGTH_SHORT).show();
            return;
        }

        CursorAdapter adapter = new CursorAdapter(this, activeCursor, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.booking_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                ImageView imgBookingVehicleIcon = view.findViewById(R.id.imgBookingVehicleIcon);
                TextView tvBookingTitle        = view.findViewById(R.id.tvBookingTitle);
                TextView tvBookingVehicleInfo  = view.findViewById(R.id.tvBookingVehicleInfo);
                TextView tvBookingStatus       = view.findViewById(R.id.tvBookingStatus);

                String bookingId   = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String serviceType = cursor.getString(cursor.getColumnIndexOrThrow("serviceType"));
                String bookingDate = cursor.getString(cursor.getColumnIndexOrThrow("bookingDate"));
                String status      = cursor.getString(cursor.getColumnIndexOrThrow("serviceStatus"));
                String brand       = getSafeColumn(cursor, "vehicleBrand");
                String model       = getSafeColumn(cursor, "vehicleModel");
                String reg         = getSafeColumn(cursor, "registrationNumber");
                String vehicleType = getSafeColumn(cursor, "vehicleType");

                tvBookingTitle.setText("Booking #" + bookingId + "  |  " + serviceType);
                tvBookingVehicleInfo.setText("Vehicle Info: " + brand + " " + model + " (" + reg + ")  |  Date: " + bookingDate);
                tvBookingStatus.setText("Status: " + status + "  (Tap to update)");

                if (vehicleType.equalsIgnoreCase("Truck")) {
                    imgBookingVehicleIcon.setImageResource(R.drawable.ic_truck);
                } else if (vehicleType.equalsIgnoreCase("Motorcycle") || vehicleType.equalsIgnoreCase("Bike")) {
                    imgBookingVehicleIcon.setImageResource(R.drawable.ic_motorcycle);
                } else {
                    imgBookingVehicleIcon.setImageResource(R.drawable.ic_sedan);
                }
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
                    int pos = tabLayoutUpdateStatus.getSelectedTabPosition();
                    loadBookings(TAB_FILTERS[pos < 0 ? 0 : pos]);
                } else {
                    Toast.makeText(UpdateServiceStatusActivity.this, "Error updating status", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    private String getSafeColumn(Cursor cursor, String column) {
        int idx = cursor.getColumnIndex(column);
        if (idx == -1 || cursor.isNull(idx)) return "N/A";
        String val = cursor.getString(idx);
        return (val == null || val.isEmpty()) ? "N/A" : val;
    }
}
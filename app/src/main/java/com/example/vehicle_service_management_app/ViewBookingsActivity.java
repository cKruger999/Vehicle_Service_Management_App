package com.example.vehicle_service_management_app;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class ViewBookingsActivity extends AppCompatActivity {

    ListView lvBookings;
    TabLayout tabLayoutBookings;
    DatabaseHelper myDb;
    String mechanicName;

    private static final String[] TAB_FILTERS = {"In Progress", "Pending", "Completed", "All"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        myDb = new DatabaseHelper(this);
        mechanicName = getIntent().getStringExtra("MECHANIC_NAME");
        lvBookings = findViewById(R.id.lvBookings);
        tabLayoutBookings = findViewById(R.id.tabLayoutBookings);

        for (String filter : TAB_FILTERS) {
            tabLayoutBookings.addTab(tabLayoutBookings.newTab().setText(filter));
        }

        tabLayoutBookings.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        int pos = tabLayoutBookings.getSelectedTabPosition();
        loadBookings(TAB_FILTERS[pos < 0 ? 0 : pos]);
    }

    private void loadBookings(String filter) {
        Cursor cursor = filter.equals("All")
                ? myDb.getBookingsByMechanic(mechanicName)
                : myDb.getBookingsByMechanicAndStatus(mechanicName, filter);

        if (cursor == null || cursor.getCount() == 0) {
            lvBookings.setAdapter(null);
            Toast.makeText(this, "No \"" + filter + "\" bookings found.", Toast.LENGTH_SHORT).show();
            return;
        }

        CursorAdapter adapter = new CursorAdapter(this, cursor, 0) {

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
                tvBookingStatus.setText("Status: " + status);

                if (vehicleType.equalsIgnoreCase("Truck")) {
                    imgBookingVehicleIcon.setImageResource(R.drawable.ic_truck);
                } else if (vehicleType.equalsIgnoreCase("Motorcycle") || vehicleType.equalsIgnoreCase("Bike")) {
                    imgBookingVehicleIcon.setImageResource(R.drawable.ic_motorcycle);
                } else {
                    imgBookingVehicleIcon.setImageResource(R.drawable.ic_sedan);
                }
            }
        };

        lvBookings.setAdapter(adapter);
    }

    private String getSafeColumn(Cursor cursor, String column) {
        int idx = cursor.getColumnIndex(column);
        if (idx == -1 || cursor.isNull(idx)) return "N/A";
        String val = cursor.getString(idx);
        return (val == null || val.isEmpty()) ? "N/A" : val;
    }
}
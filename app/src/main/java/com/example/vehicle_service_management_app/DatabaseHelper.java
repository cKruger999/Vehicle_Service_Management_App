package com.example.vehicle_service_management_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "mechanic_shop.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Customers (customerID INTEGER PRIMARY KEY AUTOINCREMENT, customerName TEXT, customerSurname TEXT, phoneNumber TEXT, emailAddress TEXT, password TEXT)");

        db.execSQL("CREATE TABLE Mechanics (mechanicID INTEGER PRIMARY KEY AUTOINCREMENT, mechanicName TEXT, specialization TEXT, yearsOfExperience TEXT, password TEXT)");

        db.execSQL("CREATE TABLE Vehicles (vehicleID INTEGER PRIMARY KEY AUTOINCREMENT, vehicleBrand TEXT, vehicleModel TEXT, registrationNumber TEXT, vehicleYear TEXT, vehicleType TEXT, customerID INTEGER)");

        db.execSQL("CREATE TABLE Bookings (bookingID INTEGER PRIMARY KEY AUTOINCREMENT, serviceType TEXT, bookingDate TEXT, serviceStatus TEXT, assignedMechanic TEXT, vehicleID INTEGER)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Customers");
        db.execSQL("DROP TABLE IF EXISTS Mechanics");
        db.execSQL("DROP TABLE IF EXISTS Bookings");
        db.execSQL("DROP TABLE IF EXISTS Vehicles");
        onCreate(db);
    }
    public String authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String role = null;

        if (username.equals("admin") && password.equals("admin123")) {
            return "Admin:-1";
        }
        Cursor custCursor = db.rawQuery("SELECT customerID FROM Customers WHERE customerName = ? AND password = ?", new String[]{username, password});
        if (custCursor.moveToFirst()) {
            String id = custCursor.getString(0);
            custCursor.close();
            return "Customer:" + id;
        }
        custCursor.close();

        Cursor mechCursor = db.rawQuery("SELECT mechanicID FROM Mechanics WHERE mechanicName = ? AND password = ?", new String[]{username, password});
        if (mechCursor.moveToFirst()) {
            String id = mechCursor.getString(0);
            mechCursor.close();
            return "Mechanic:" + id;
        }
        mechCursor.close();

        return null;
    }
    public boolean insertCustomer(String name, String surname, String phone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("customerName", name);
        cv.put("customerSurname", surname);
        cv.put("phoneNumber", phone);
        cv.put("emailAddress", email);
        cv.put("password", password);

        long result = db.insert("Customers", null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertMechanic(String name, String specialization, String experience, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("mechanicName", name);
        cv.put("specialization", specialization);
        cv.put("yearsOfExperience", experience);
        cv.put("password", password);

        long result = db.insert("Mechanics", null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertVehicle(String brand, String model, String regNumber, String year, String type, String customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("vehicleBrand", brand);
        cv.put("vehicleModel", model);
        cv.put("registrationNumber", regNumber);
        cv.put("vehicleYear", year);
        cv.put("vehicleType", type);
        cv.put("customerID", customerId);

        long result = db.insert("Vehicles", null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getCustomerDashboardData(String customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Vehicles.*,Vehicles.vehicleID AS _id, Bookings.serviceStatus FROM Vehicles LEFT JOIN Bookings ON Vehicles.vehicleID = Bookings.vehicleID WHERE Vehicles.customerID = ? ORDER BY Bookings.bookingID DESC", new String[]{customerId});
    }
    public Cursor getAllCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Customers", null);
    }

    public Cursor getCustomerById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Customers WHERE customerID = ?", new String[]{id});
    }

    // --- Mechanics ---
    public Cursor getAllMechanics() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Mechanics", null);
    }

    public Cursor getMechanicById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Mechanics WHERE mechanicID = ?", new String[]{id});
    }

    // --- Vehicles ---
    public Cursor getAllVehicles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Vehicles", null);
    }

    public Cursor getVehicleById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Vehicles WHERE vehicleID = ?", new String[]{id});
    }

    // =========================================================================================
    // UPDATE METHODS
    // =========================================================================================

    public boolean updateCustomer(String id, String name, String surname, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("customerName", name);
        cv.put("customerSurname", surname);
        cv.put("phoneNumber", phone);
        cv.put("emailAddress", email);

        long result = db.update("Customers", cv, "customerID=?", new String[]{id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateMechanic(String id, String name, String specialization, String experience) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("mechanicName", name);
        cv.put("specialization", specialization);
        cv.put("yearsOfExperience", experience);

        long result = db.update("Mechanics", cv, "mechanicID=?", new String[]{id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateVehicle(String id, String brand, String model, String reg, String year, String custId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("vehicleBrand", brand);
        cv.put("vehicleModel", model);
        cv.put("registrationNumber", reg);
        cv.put("vehicleYear", year);
        cv.put("customerID", custId);

        long result = db.update("Vehicles", cv, "vehicleID=?", new String[]{id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // =========================================================================================
    // DELETE METHODS
    // =========================================================================================

    public boolean deleteCustomer(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Customers", "customerID=?", new String[]{id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteMechanic(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Mechanics", "mechanicID=?", new String[]{id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteVehicle(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Vehicles", "vehicleID=?", new String[]{id});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getRoleBasedId(String username, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String id = "";
        Cursor cursor = null;

        if (role.equalsIgnoreCase("Customer")) {
            cursor = db.rawQuery("SELECT customerID FROM Customers WHERE customerName = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                id = cursor.getString(0);
            }
        } else if (role.equalsIgnoreCase("Mechanic")) {
            cursor = db.rawQuery("SELECT mechanicID FROM Mechanics WHERE mechanicName = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                id = cursor.getString(0);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return id;
    }

}

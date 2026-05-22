package com.example.vehicle_service_management_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "mechanic_shop.db";
    private static final int DATABASE_VERSION = 3;
    public String TABLE_USERS = "Users";
    public String COL_USERNAME = "username";
    public String COL_PASSWORD = "password";
    public  String COL_ROLE = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Customers (customerID INTEGER PRIMARY KEY AUTOINCREMENT, customerName TEXT, customerSurname TEXT, phoneNumber TEXT, emailAddress TEXT)");

        db.execSQL("CREATE TABLE Mechanics (mechanicID INTEGER PRIMARY KEY AUTOINCREMENT, mechanicName TEXT, specialization TEXT, yearsOfExperience TEXT)");

        db.execSQL("CREATE TABLE Bookings (bookingID INTEGER PRIMARY KEY AUTOINCREMENT, serviceType TEXT, bookingDate TEXT, serviceStatus TEXT, assignedMechanic TEXT, vehicleID INTEGER)");

        db.execSQL("CREATE TABLE Vehicles (vehicleID INTEGER PRIMARY KEY AUTOINCREMENT, vehicleBrand TEXT, vehicleModel TEXT, registrationNumber TEXT, vehicleYear TEXT, vehicleType TEXT, customerID INTEGER)");
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(createUsersTable);
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password, role) VALUES ('Phil', 'admin123', 'Admin')");

        //A quick check for customer dash
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password, role) VALUES ('John', 'cust123', 'Customer')");
        db.execSQL("INSERT INTO Customers (customerName, customerSurname, phoneNumber, emailAddress) VALUES ('John', 'Doe', '0821112222', 'john@email.com')");


        db.execSQL("INSERT INTO Vehicles (vehicleBrand, vehicleModel, registrationNumber, vehicleYear, vehicleType, customerID) VALUES ('Toyota', 'Hilux', 'CA-123456', '2022', 'Truck', 1)");
        db.execSQL("INSERT INTO Vehicles (vehicleBrand, vehicleModel, registrationNumber, vehicleYear, vehicleType, customerID) VALUES ('Honda', 'Civic', 'CY-987654', '2020', 'Sedan', 1)");

        db.execSQL("INSERT INTO Bookings (serviceType, bookingDate, serviceStatus, assignedMechanic, vehicleID) VALUES ('Minor Service', '2026-05-25', 'In Progress', 'Alex Smith', 1)");
        db.execSQL("INSERT INTO Bookings (serviceType, bookingDate, serviceStatus, assignedMechanic, vehicleID) VALUES ('Brake Replacement', '2026-05-26', 'Pending', 'Alex Smith', 2)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS Customers");
        db.execSQL("DROP TABLE IF EXISTS Mechanics");
        db.execSQL("DROP TABLE IF EXISTS Bookings");
        db.execSQL("DROP TABLE IF EXISTS Vehicles");
        onCreate(db);
    }
    public String authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String role = null;

        String query = "SELECT " + COL_ROLE + " FROM " + TABLE_USERS +
                " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        if (cursor.moveToFirst()) {
            role = cursor.getString(0);
        }

        cursor.close();
        return role;
    }
    public boolean insertCustomer(String name, String surname, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("customerName", name);
        cv.put("customerSurname", surname);
        cv.put("phoneNumber", phone);
        cv.put("emailAddress", email);

        long result = db.insert("Customers", null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertMechanic(String name, String specialization, String experience) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("mechanicName", name);
        cv.put("specialization", specialization);
        cv.put("yearsOfExperience", experience);

        long result = db.insert("Mechanics", null, cv);
        return result != -1;
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
        return result != -1;
    }
    public Cursor getCustomerDashboardData(String customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT Vehicles.*,Vehicles.vehicleID AS _id, Bookings.serviceStatus FROM Vehicles LEFT JOIN Bookings ON Vehicles.vehicleID = Bookings.vehicleID WHERE Vehicles.customerID = ? ORDER BY Bookings.bookingID DESC", new String[]{customerId});
    }
    public Cursor getAllCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Customers", null);
    }
    public Cursor getCustomerById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Customers WHERE customerID = ?", new String[]{id});
    }

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

    public boolean deleteCustomer(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Customers", "customerID=?", new String[]{id});
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

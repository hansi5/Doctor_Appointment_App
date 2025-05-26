package com.example.project;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class appdate extends AppCompatActivity {
    private DatePicker datePicker;
    private Spinner timeSpinner;
    private Button b6, back;

    private String id, hospital, doctor, cause, selectedDate, specialist, selectedTime;
    private SQLiteDatabase db;
    int token;
    private ArrayList<String> timeList;
    private ArrayAdapter<String> timeAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdate);

        // Initialize UI Elements
        datePicker = findViewById(R.id.datePicker_date);
        timeSpinner = findViewById(R.id.spinner_time); // Make sure this matches your XML ID
        b6 = findViewById(R.id.btn_date);
        SharedPreferences sharedPreferences = getSharedPreferences("id", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "Default Name");

        // Retrieve data from previous intent
        Intent i = getIntent();
        hospital = i.getStringExtra("hospital");
        doctor = i.getStringExtra("doctor");
        cause = i.getStringExtra("cause");
        specialist = i.getStringExtra("specialist");

        back = findViewById(R.id.back8);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appdate.this, new_appointment1.class);
                startActivity(intent);
            }
        });

//        token = 0;

        // Open or Create Database
        db = openOrCreateDatabase("db", MODE_PRIVATE, null);

        // Create appointments table if it doesn't exist

        db.execSQL("CREATE TABLE IF NOT EXISTS appointments (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "doctor_name varchar, hospital_name varchar, cause varchar, appointment_date varchar, appointment_time varchar, user_id varchar, token int)");

        // Load available time slots for the selected doctor
        loadAvailableTimeSlots();

        // Set Date Picker Listener
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                (view, year, month, day) -> selectedDate = year + "-" + (month + 1) + "-" + day);

        // Handle Confirm Appointment Button Click
        b6.setOnClickListener(v -> confirmAppointment());
    }

    // Method to load available time slots from the database
    private void loadAvailableTimeSlots() {
        timeList = new ArrayList<>();
        timeList.add("Select Time"); // Default option

        // Query to fetch available time slots for the selected doctor
        Cursor cursor = db.rawQuery("SELECT time_slot FROM time_table WHERE specialist = ?", new String[]{specialist});
        while (cursor.moveToNext()) {
            timeList.add(cursor.getString(0));
        }
        cursor.close();

        // Set adapter for time spinner
        timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        timeSpinner.setAdapter(timeAdapter);

        // Handle time selection
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Nothing needed here, selected time is taken care of in confirmAppointment()
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Method to check if the doctor is available at the selected date & time
    private boolean isDoctorAvailable(String doctor, String date, String time) {
        if (doctor == null || doctor.isEmpty() || date == null || date.isEmpty() || time == null || time.isEmpty()) {
            Log.e("ERROR", "Invalid parameters passed to check availability!");
            return false; // Invalid input, treat as unavailable
        }

        Cursor c = null;
        boolean isAvailable = false;

        try {
            // Check if the doctor is already booked at the given date and time
            c = db.rawQuery(
                    "SELECT * FROM appointments WHERE doctor_name = ? AND appointment_date = ? AND appointment_time = ?",
                    new String[]{doctor, date, time});

            isAvailable = !c.moveToFirst(); // If no record, doctor is available
        } catch (Exception e) {
            Log.e("DB_ERROR", "Error while checking availability: " + e.getMessage());
            isAvailable = false;
        } finally {
            if (c != null) {
                c.close(); // Close cursor to avoid memory leak
            }
        }

        Log.d("DEBUG", "Doctor Availability: " + (isAvailable ? "Available" : "Not Available"));
        return isAvailable;
    }



    // Confirm Appointment
    private void confirmAppointment() {

        SharedPreferences sharedPreferences = getSharedPreferences("id", MODE_PRIVATE);
        token = sharedPreferences.getInt("token", 0);
        token += 1;

        selectedTime = timeSpinner.getSelectedItem().toString();

        // Format selected date properly
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month is 0-indexed
        int year = datePicker.getYear();
        selectedDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);

        Log.d("DEBUG", "Selected Date: " + selectedDate + ", Selected Time: " + selectedTime);

        // Validate user selection
        if (selectedDate == null || selectedTime.equals("Select Time")) {
            Toast.makeText(getApplicationContext(), "Please select a valid date and time", Toast.LENGTH_LONG).show();
            return;
        }

        // Check doctor availability
        if (!isDoctorAvailable(doctor, selectedDate, selectedTime)) {
            Toast.makeText(getApplicationContext(), "Doctor is already booked at this time!" + selectedDate + " " + selectedTime, Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the string to Date
            Date d = sdf.parse(selectedDate);

            // Get current date
            Date currentDate = new Date();

            // Compare dates
            if (d.before(currentDate)) {
                Toast.makeText(this, "Appointment on past date is not allowed!", Toast.LENGTH_SHORT).show();
            }
            else {
                try {

                    db.execSQL("INSERT INTO appointments (doctor_name, hospital_name, cause, appointment_date, appointment_time, user_id, token) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?)",
                            new Object[]{doctor, hospital, cause, selectedDate, selectedTime, id, token});

//            Toast.makeText(getApplicationContext(), "Appointment Confirmed!", Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("token", token);
                    editor.apply();
                    showConfirmationDialog();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error while booking appointment!", Toast.LENGTH_LONG).show();
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Invalid date format!");
        }
    }
    private void sendSMS(String phoneNumber, String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        } else {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(this, "Appointment details sent via SMS", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "SMS failed to send", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }






    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment Status");
        builder.setMessage("APPOINTMENT CONFIRMED");

        // "OK" button to navigate after confirmation
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Close the pop-up

                // Redirect to appointment page after confirmation
                Intent intent = new Intent(appdate.this, appointment.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


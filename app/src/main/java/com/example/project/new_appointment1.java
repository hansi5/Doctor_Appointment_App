package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class new_appointment1 extends AppCompatActivity {

    private Spinner spinnerHospital, spinnerCause, spinnerDoctor;
    private ArrayList<String> hospitalList, causeList, doctorList;
    private ArrayAdapter<String> hospitalAdapter, causeAdapter, doctorAdapter;
    private SQLiteDatabase db;
    String selectedHospital = "", selectedCause = "", selectedDoctor = "";

    Button b5, back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment1);

        // Initialize Spinners
        spinnerHospital = findViewById(R.id.spinner_hospital);
        spinnerCause = findViewById(R.id.spinner_cause);
        spinnerDoctor = findViewById(R.id.spinner_doctor);

        db = openOrCreateDatabase("db", MODE_PRIVATE, null);

        back = findViewById(R.id.back9);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new_appointment1.this, appointment.class);
                startActivity(intent);
            }
        });

        // Initially disable second and third spinners
        spinnerCause.setEnabled(false);
        spinnerDoctor.setEnabled(false);

        // Load hospitals
        loadHospitals();

        // Handle hospital selection
        spinnerHospital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Ignore "Select a hospital"
                    selectedHospital = hospitalList.get(position);
                    spinnerCause.setEnabled(true);
                    loadCauses();
                } else {
                    selectedHospital = "";
                    spinnerCause.setEnabled(false);
                    spinnerDoctor.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Handle cause selection
        spinnerCause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Ignore "Select a cause"
                    selectedCause = causeList.get(position);
                    spinnerDoctor.setEnabled(true);
                    loadDoctors(selectedHospital, selectedCause);
                } else {
                    selectedCause = "";
                    spinnerDoctor.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Handle doctor selection
        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Ignore "Select a doctor"
                    selectedDoctor = doctorList.get(position);
                    Toast.makeText(new_appointment1.this, "Selected Doctor: " + selectedDoctor, Toast.LENGTH_SHORT).show();
                } else {
                    selectedDoctor = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        b5 = findViewById(R.id.btn_app);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedHospital.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "Please select the hospital", Toast.LENGTH_LONG).show();
                }
                else if (selectedCause.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select the cause", Toast.LENGTH_LONG).show();
                }
                else if (selectedDoctor.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select the doctor", Toast.LENGTH_LONG).show();
                }
                else {
                    String selectedSpecialist = selectedDoctor.substring(selectedDoctor.indexOf("(") + 1, selectedDoctor.indexOf(")")).trim();
                    Intent intent = new Intent(new_appointment1.this, appdate.class);
                    intent.putExtra("hospital", selectedHospital);
                    intent.putExtra("cause", selectedCause);
                    intent.putExtra("doctor", selectedDoctor);
                    intent.putExtra("specialist", selectedSpecialist);
                    Toast.makeText(new_appointment1.this, selectedHospital + " " + selectedDoctor + " " + selectedCause, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });
    }

    // Load hospital names into the first spinner
    private void loadHospitals() {
        hospitalList = new ArrayList<>();
        hospitalList.add("Select a hospital"); // Default value
        Cursor cursor = db.rawQuery("SELECT DISTINCT d_name FROM doctor", null);
        while (cursor.moveToNext()) {
            hospitalList.add(cursor.getString(0));
        }
        cursor.close();
        hospitalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hospitalList);
        spinnerHospital.setAdapter(hospitalAdapter);
    }

    // Load causes into the second spinner
    private void loadCauses() {
        causeList = new ArrayList<>();
        causeList.add("Select a cause"); // Default value
        Cursor cursor = db.rawQuery("SELECT DISTINCT c_name FROM causes", null);
        while (cursor.moveToNext()) {
            causeList.add(cursor.getString(0));
        }
        cursor.close();
        causeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, causeList);
        spinnerCause.setAdapter(causeAdapter);
    }

    // Load doctors based on selected hospital and cause
    private void loadDoctors(String hospital, String cause) {
        doctorList = new ArrayList<>();
        doctorList.add("Select a doctor"); // Default value
        Cursor cursor = db.rawQuery(
                "SELECT DISTINCT d.hospital, d.specialists FROM doctor d " +
                        "JOIN causes c ON d.specialists = c.specialists " +
                        "WHERE d.d_name = ? AND c.c_name = ?", new String[]{hospital, cause});

        while (cursor.moveToNext()) {
            doctorList.add(cursor.getString(0) + " (" + cursor.getString(1) + ")");        }
        cursor.close();

        // Show message if no doctors found
        if (doctorList.size() == 1) {
            doctorList.add("No doctor available");
        }

        doctorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doctorList);
        spinnerDoctor.setAdapter(doctorAdapter);
    }
}

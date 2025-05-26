package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class appointment extends AppCompatActivity {
    Button b3;
    SQLiteDatabase db;
    TextView l1, l3, uName;
    LinearLayout appointmentContainer;
    String id;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);
        b3 = findViewById(R.id.b);
        uName = findViewById(R.id.userName);
        SharedPreferences sharedPreferences = getSharedPreferences("id", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "Default Name");
        db = openOrCreateDatabase("db", MODE_PRIVATE, null);

        Cursor nameCursor = db.rawQuery("SELECT name FROM info where email = '" + id + "'", null);
        while (nameCursor.moveToNext()) {
            String userName = nameCursor.getString(0);
            uName.setText("Welcome " + userName + " !");
        }
        nameCursor.close();

        l1 = findViewById(R.id.l1);
        l3 = findViewById(R.id.infoEdit);

        try {
            loadAppointments();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(appointment.this, new_appointment1.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
                startActivity(intent);
//                finish();

            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appointment.this, edit.class);
                startActivity(intent);
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(appointment.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadAppointments() throws ParseException {

        db.execSQL("CREATE TABLE IF NOT EXISTS appointments (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "doctor_name varchar, hospital_name varchar, cause varchar, appointment_date varchar, appointment_time varchar, user_id varchar, token int)");

        Cursor cursor = db.rawQuery("SELECT doctor_name, hospital_name, cause, appointment_date, appointment_time, token FROM appointments where user_id = '" + id + "' order by token desc", null);

        if (cursor.moveToFirst()) {
            do {
                String doctorName = cursor.getString(0);
                String hospitalName = cursor.getString(1);
                String cause = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                int token = cursor.getInt(5);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                Date inputDate = sdf.parse(date);

                Date currentDate = new Date();

                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.add(Calendar.MONTH, -1);
                Date oneMonthAgo = cal.getTime();

                if (inputDate.after(oneMonthAgo)) {


                    // Create a LinearLayout dynamically
                    LinearLayout linearLayout = new LinearLayout(this);

                    // Set layout parameters
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    linearLayout.setLayoutParams(layoutParams);

                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.RECTANGLE);
                    drawable.setColor(Color.parseColor("#27445D")); // Background color
                    drawable.setCornerRadius(30);
                    linearLayout.setBackground(drawable);

                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setPadding(30, 20, 30, 20);
                    layoutParams.setMargins(20, 30, 20, 30);

                    // Create a TextView dynamically
                    TextView t1 = new TextView(this);
                    t1.setText("Hospital name: " + hospitalName);
                    t1.setTextSize(18);
                    t1.setPadding(10, 10, 10, 10);
                    t1.setTextColor(getResources().getColor(android.R.color.white));

                    TextView t2 = new TextView(this);
                    t2.setText("Doctor name: " + doctorName);
                    t2.setTextSize(18);
                    t2.setPadding(10, 10, 10, 10);
                    t2.setTextColor(getResources().getColor(android.R.color.white));

                    TextView t3 = new TextView(this);
                    t3.setText("Cause: " + cause);
                    t3.setTextSize(18);
                    t3.setPadding(10, 10, 10, 10);
                    t3.setTextColor(getResources().getColor(android.R.color.white));

                    TextView t4 = new TextView(this);
                    t4.setText("Date: " + date);
                    t4.setTextSize(18);
                    t4.setPadding(10, 10, 10, 10);
                    t4.setTextColor(getResources().getColor(android.R.color.white));

                    TextView t5 = new TextView(this);
                    t5.setText("Time: " + time);
                    t5.setTextSize(18);
                    t5.setPadding(10, 10, 10, 10);
                    t5.setTextColor(getResources().getColor(android.R.color.white));

                    TextView t6 = new TextView(this);
                    t6.setText("Token number: " + token);
                    t6.setTextSize(18);
                    t6.setPadding(10, 10, 10, 10);
                    t6.setTextColor(getResources().getColor(android.R.color.white));

                    TextView t8 = new TextView(this);
                    t8.setTextSize(18);
                    t8.setPadding(10, 10, 10, 10);
                    t8.setTextColor(getResources().getColor(android.R.color.white));

                    TextView t7 = new TextView(this);
                    t7.setTextSize(18);
                    t7.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    t7.setPadding(10, 15, 10, 10);
                    t7.setTextColor(getResources().getColor(android.R.color.white));


                    // Define the date format


                    try {
                        // Parse the string to Date
                        Date d = sdf.parse(date);

                        // Compare dates
                        if (d.before(currentDate)) {
                            t8.setText("Status: Met");
                        } else {
                            t7.setText("Cancel");
                            t7.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    db.execSQL("Delete from appointments where token = " + token);
                                    Toast.makeText(appointment.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(appointment.this, appointment.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            t8.setText("Status: Upcoming");
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                        System.out.println("Invalid date format!");
                    }


                    // Add TextView to LinearLayout
                    linearLayout.addView(t1);
                    linearLayout.addView(t2);
                    linearLayout.addView(t3);
                    linearLayout.addView(t4);
                    linearLayout.addView(t5);
                    linearLayout.addView(t6);
                    linearLayout.addView(t8);
                    linearLayout.addView(t7);

                    // Add LinearLayout to parent layout
                    LinearLayout parentLayout = findViewById(R.id.appContainer);  // Parent layout from XML
                    parentLayout.addView(linearLayout);

                } // if close
                    // Add a new appointment card
                }
                while (cursor.moveToNext()) ;

        }
        else {
            LinearLayout linearLayout = new LinearLayout(this);

            // Set layout parameters
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setGravity(0);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(30, 20, 30, 20);
            layoutParams.setMargins(20, 30, 20, 30);

            TextView t5 = new TextView(this);
            t5.setText("No appointments made yet.");
            t5.setTextSize(18);
            t5.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t5.setPadding(10, 10, 10, 10);
            t5.setTextColor(getResources().getColor(android.R.color.black));
            linearLayout.addView(t5);

            LinearLayout parentLayout = findViewById(R.id.appContainer);  // Parent layout from XML
            parentLayout.addView(linearLayout);
        }
        cursor.close();

    }


}
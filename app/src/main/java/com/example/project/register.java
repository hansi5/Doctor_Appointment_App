package com.example.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;


public class register extends AppCompatActivity {

    Button reg, back;
    SQLiteDatabase db;
    String name, sex, email, address, phone, selectedDate;
    EditText n, a, e, ad, p;
    TextView li;
    RadioGroup rg;
    RadioButton rb;
    int age, r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        db = openOrCreateDatabase("db", MODE_PRIVATE, null);
        db.execSQL("create table if not exists info (name varchar, date date, age int, sex varchar, email varchar, address varchar, phone varchar)");
        rg = findViewById(R.id.rg);
        n = findViewById(R.id.name);
        a = findViewById(R.id.age);
        e = findViewById(R.id.email);
        ad = findViewById(R.id.address);
        p = findViewById(R.id.phone);
        li = findViewById(R.id.li);

        li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, log.class);
                startActivity(intent);
                finish();
            }
        });

        back = findViewById(R.id.back3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        DatePicker datePicker = findViewById(R.id.datePicker);
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    selectedDate = year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                });

        reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                r = rg.getCheckedRadioButtonId();
                rb = findViewById(r);
                if (r != -1) {
                    rb = findViewById(r);
                    sex = rb.getText().toString();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (n.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                name = n.getText().toString();

                if (a.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Age cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                age = Integer.parseInt(a.getText().toString());

                if (e.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                email = e.getText().toString();

                if (ad.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }
                address = ad.getText().toString();

                if (p.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPhoneNumber(p.getText().toString())) {
                    Toast.makeText(register.this, "Give correct phone number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                phone = p.getText().toString();

                db.execSQL("insert into info values ('" + name + "','" + selectedDate +
                        "', " +  age + ",'" + sex + "', '" + email + "', '" + address
                        + "', '" + phone + "')");

                Toast.makeText(getApplicationContext(), "inserted", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(register.this, log.class);
                startActivity(intent);
            }
        });
    }
    public static boolean isValidPhoneNumber(String number) {
        // Define a regex pattern for a 10-digit phone number
        String regex = "^[6-9]\\d{9}$"; // Starts with 6-9 and has 10 digits

        // Match the input against the pattern
        return Pattern.matches(regex, number);
    }
}
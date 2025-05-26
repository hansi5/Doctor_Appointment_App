package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class edit extends AppCompatActivity {

    Button r, back;
    SQLiteDatabase db;
    String d1, d2, d3, d4, d5, d7;
    int d6;
    EditText n1, n3, n4, n7;
    TextView n5, n6, n2;
    int age;
    String id;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        SharedPreferences sharedPreferences = getSharedPreferences("id", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "Default Name");
//
        n1 = findViewById(R.id.en1); // name
        n2 = findViewById(R.id.en7); // date
        n3 = findViewById(R.id.en2); // age
        n5 = findViewById(R.id.en3); // gender
        n6 = findViewById(R.id.en8); // email
        n4 = findViewById(R.id.en4); // address
        n7 = findViewById(R.id.en5); // phone

        r = findViewById(R.id.e_b);

        db = openOrCreateDatabase("db", MODE_PRIVATE, null);

        Cursor  c = db.rawQuery("select * from info where email = '" + id + "'", null);

        while(c.moveToNext()) {
            n1.setText(c.getString(0));
            n2.setText(c.getString(1));
            n3.setText(String.valueOf(c.getInt(2)));
            n5.setText(c.getString(3));
            n6.setText(c.getString(4));
            n4.setText(c.getString(5));
            n7.setText(c.getString(6));
        }

        c.close();

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (n1.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (n3.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Age cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (n4.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (n7.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                d1 = n1.getText().toString();
                d6 = Integer.parseInt(n3.getText().toString());
                d2 = n4.getText().toString();
                d3 = n7.getText().toString();

                db.execSQL("update info set name = '" + d1 + "', age = " + d6 + ", address = '" + d2 + "', phone = '" + d3 + "' where email = '" + id + "'");
                Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_LONG).show();

                Intent u = new Intent(edit.this, appointment.class);
                startActivity(u);

            }
        });

        back = findViewById(R.id.back7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(edit.this, appointment.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class log extends AppCompatActivity {

    SQLiteDatabase db;
    Button l, back;
    EditText email, pass;
    TextView sign;
    String e, p, p_d;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log);

        db = openOrCreateDatabase("db", MODE_PRIVATE, null);
        l = findViewById(R.id.login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        sign = findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(log.this, register.class);
                startActivity(intent);
                finish();
            }
        });

        back = findViewById(R.id.back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(log.this, MainActivity.class);
                startActivity(intent);
            }
        });

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                e = email.getText().toString();
                p = pass.getText().toString();

                try{
                    Cursor c = db.rawQuery("select email, phone from info where email = '" + e +"'", null);
                    while (c.moveToNext()){
                        String e_d = c.getString(0);
                        p_d = c.getString(1);
                    }
                    if (p.equals(p_d)) {
                        Intent intent = new Intent(log.this, appointment.class);
                        intent.putExtra("id", e);
                        SharedPreferences sharedPreferences = getSharedPreferences("id", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("id", e);
                        editor.apply();
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(log.this, "Password is incorrect, try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "You don't have an account, please register", Toast.LENGTH_LONG).show();
                }




            }
        });


    }
}
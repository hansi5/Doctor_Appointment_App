package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class hospital_detail  extends AppCompatActivity {
    Button back;
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.hospital_detail);

            TextView hospitalNameText = findViewById(R.id.hospitalName);
            TextView hospitalA = findViewById(R.id.hospitalAbout);
        TextView hospitalD = findViewById(R.id.hospitalDoc);
        TextView hospitalS = findViewById(R.id.hospitalSpec);
        TextView hospitalL = findViewById(R.id.hospitalLoc);
        TextView hospitalT = findViewById(R.id.hospitalTime);
        ImageView pp1 = findViewById(R.id.p1);
        ImageView pp2 = findViewById(R.id.p2);
        ImageView pp3 = findViewById(R.id.p3);

        back = findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(hospital_detail.this, MainActivity.class);
                startActivity(intent);
            }
        });

            String hospitalName = getIntent().getStringExtra("hospitalName");
            String hospitalTime = getIntent().getStringExtra("hospitalTime");
            String hospitalLoc = getIntent().getStringExtra("hospitalLoc");
            String hospitalDoc= getIntent().getStringExtra("hospitalDoc");
            String hospitalSpec = getIntent().getStringExtra("hospitalSpec");
        int room = getIntent().getIntExtra("room", R.drawable.room1);
        int equip = getIntent().getIntExtra("equip", R.drawable.e1);
        String hospitalAbout = getIntent().getStringExtra("hospitalAbout");
        int recep = getIntent().getIntExtra("recep", R.drawable.r1);



        hospitalNameText.setText(hospitalName);
            hospitalA.setText(hospitalAbout);
        hospitalD.setText(hospitalDoc);
        hospitalS.setText(hospitalSpec);
        hospitalT.setText(hospitalTime);
        hospitalL.setText(hospitalLoc);
        pp1.setImageResource(recep);
        pp2.setImageResource(room);
        pp3.setImageResource(equip);
        }
    }



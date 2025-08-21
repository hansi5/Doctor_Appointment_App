package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.project.R;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button log, reg;
    SQLiteDatabase db;
    // Hospital logos and descriptions
    int[] hospitalLogos = {R.drawable.lifeline_hospital, R.drawable.healing_hub, R.drawable.shifa_hospital,R.drawable.healthharbor};
    String[] hospitalNames = {"LIFELINE HOSPITAL", "HEALING HUB", "SHIFA HOSPITAL","HEALTH HARBOR"};
    int[] room = {R.drawable.room1, R.drawable.room2, R.drawable.room3, R.drawable.room4};
    int[] equipment = {R.drawable.e1, R.drawable.e2, R.drawable.e3, R.drawable.e4};
    int[] recep = {R.drawable.r1, R.drawable.r2, R.drawable.r3, R.drawable.r4};
    String[] hospitalAbout = {"Lifeline Hospital is a multi-specialty hospital known for its state-of-the-art medical facilities and highly experienced doctors. It provides top-notch patient care and advanced treatments in various fields.",
    "Healing Hub is a renowned hospital focusing on holistic treatments and advanced healthcare solutions. It combines traditional and modern medicine to ensure the best patient outcomes.",
    "Shifa Hospital is a leading healthcare facility providing specialized medical services with a patient-centric approach. It is known for its expert doctors and modern medical equipment.",
    "Health Harbor is a premier hospital offering specialized healthcare services, particularly in gynecology and cancer treatments. It is recognized for its patient-friendly environment and expert care."};
    String[] hospitalSpec = {"Cardiology\n" +  "Neurology\n" +  "Orthopedics\n" +  "Oncology\n" + "Pediatrics.",
            "Alternative Medicine\n" + "Physiotherapy\n" +"Pain Management\n"+ "Mental Health & Counseling.",
            "General Physician\n" + "Gastroenterology\n" + "Dermatology\n" + "Urology\n" + "Gynecology.",
            "Gynecology\n" + "Oncology (Cancer Treatment)\n" +  "Endocrinology\n" + "Rheumatology." };

    String[] hospitalLocation = {"123, Main Street, City Center, ABC Town, XYZ Country.",
            "321, Wellness Drive, Uptown, ABC Town, XYZ Country.",
            "456, Green Avenue, Downtown, ABC Town, XYZ Country.",
            "789, Wellness Street, Midtown, ABC Town, XYZ Country."};
    String[] hospitalDoc = {"Total: 20 Specialist Doctors",
            "Total: 20 Specialist Doctors",
            "Total: 20 Specialist Doctors",
            "Total: 20 Specialist Doctors"};
    String[] hospitalTime = {"Monday – Saturday: 8:00 AM – 10:00 PM\n" +
            "Sunday: 9:00 AM – 5:00 PM",
            "Monday – Friday: 8:30 AM – 9:00 PM\n" +
                    "Saturday: 9:00 AM – 5:00 PM\n" +
                    "Sunday: Closed",
            "Monday – Saturday: 7:00 AM – 9:00 PM\n" +
                    "Sunday: Emergency Services Only.",
            "Monday – Friday: 9:00 AM – 8:00 PM\n" +
                    "Saturday – Sunday: 10:00 AM – 6:00 PM"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = findViewById(R.id.log1);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, log.class);
                startActivity(intent);
            }
        });
        reg = findViewById(R.id.reg1);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
            }
        });
        // Load hospital logos dynamically
        LinearLayout hospitalContainer = findViewById(R.id.hospitalContainer);
        for (int i = 0; i < hospitalLogos.length; i++) {
            final int index = i;

            ImageView logo = new ImageView(this);
            logo.setImageResource(hospitalLogos[i]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
            params.setMargins(30, 0, 30, 0);  // Add space between logos
            logo.setLayoutParams(params);

            logo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            logo.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, hospital_detail.class);
                intent.putExtra("hospitalName", hospitalNames[index]);
                intent.putExtra("hospitalLoc", hospitalLocation[index]);
                intent.putExtra("hospitalSpec", hospitalSpec[index]);
                intent.putExtra("hospitalDoc", hospitalDoc[index]);
                intent.putExtra("hospitalTime", hospitalTime[index]);
                intent.putExtra("hospitalAbout", hospitalAbout[index]);
                intent.putExtra("room", room[index]);
                intent.putExtra("recep", recep[index]);
                intent.putExtra("equip", equipment[index]);
                startActivity(intent);
            });

            hospitalContainer.addView(logo);
        }
        db = openOrCreateDatabase("db", MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS time_table (specialist varchar, time_slot varchar)");
        db.execSQL("create table if not exists causes (c_name varchar(250), specialists varchar(250))");
        db.execSQL("create table if not exists doctor (d_name varchar(250), hospital varchar(255), specialists varchar(250))");

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM causes", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            db.execSQL("INSERT INTO causes VALUES" +
                    "('Burning or stinging', 'Dermatologist')," +
                    "('Cracked skin', 'Dermatologist')," +
                    "('Dull ache (teeth)', 'Dentist')," +
                    "('Sharp pain (teeth)', 'Dentist')," +
                    "('Swelling in gums (teeth)', 'Dentist')," +
                    "('Throbbing (tooth)', 'Dentist')," +
                    "('Sensitivity (tooth)', 'Dentist')," +
                    "('Chest pain', 'Cardiologist')," +
                    "('Shortness of breath', 'Pulmonologist')," +
                    "('Discomfort in breathing', 'Pulmonologist')," +
                    "('Numbness', 'Neurologist')," +
                    "('Loss of feeling', 'Neurologist')," +
                    "('Electric shock sensations', 'Neurologist')," +
                    "('Weakness', 'General Physician')," +
                    "('Involuntary movements', 'Neurologist')," +
                    "('Slurred speech', 'Neurologist')," +
                    "('Memory loss', 'Neurologist')," +
                    "('Sleep problems', 'Psychiatrist')," +
                    "('Door blurring', 'Ophthalmologist')," +
                    "('Blurry nearby objects', 'Ophthalmologist')," +
                    "('Double vision', 'Ophthalmologist')," +
                    "('Fading of colors', 'Ophthalmologist')," +
                    "('Dim vision', 'Ophthalmologist')," +
                    "('Yellowing of skin and eyes', 'Hepatologist')," +
                    "('Abdominal pain', 'Gastroenterologist')," +
                    "('Stomach pain', 'Gastroenterologist')," +
                    "('Changes in stool and urine color', 'Gastroenterologist')," +
                    "('Increased thirst', 'Endocrinologist')," +
                    "('Frequent urination', 'Urologist')," +
                    "('Extreme hunger', 'Endocrinologist')," +
                    "('Running nose', 'ENT Specialist')," +
                    "('Nausea', 'General Physician')," +
                    "('Dizziness', 'Neurologist')," +
                    "('Muscle aches', 'Rheumatologist')," +
                    "('Fatigue', 'General Physician')," +
                    "('Cough', 'Pulmonologist')," +
                    "('Headache', 'Neurologist')," +
                    "('Sneezing', 'ENT Specialist')," +
                    "('Chills', 'General Physician')," +
                    "('Diarrhea', 'Gastroenterologist')," +
                    "('Sore throat', 'ENT Specialist')," +
                    "('Itching', 'Dermatologist')," +
                    "('Redness', 'Dermatologist')," +
                    "('Rash', 'Dermatologist')," +
                    "('Swelling', 'General Physician')," +
                    "('Bumps or blisters', 'Dermatologist')," +
                    "('Fever', 'General Physician')," +
                    "('Joint pain', 'Rheumatologist')," +
                    "('Weight loss', 'Endocrinologist')," +
                    "('Persistent sadness', 'Psychiatrist')," +
                    "('Guilt', 'Psychiatrist')," +
                    "('Loss of interest', 'Psychiatrist')," +
                    "('Changes in sleep', 'Psychiatrist')," +
                    "('Unusual sleep', 'Psychiatrist')," +
                    "('Burning and stinging', 'Dermatologist')," +
                    "('Climbed vision', 'Ophthalmologist')");
        }


        cursor = db.rawQuery("SELECT COUNT(*) FROM doctor", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            db.execSQL("INSERT INTO doctor VALUES" +
                    "('Shifa Hospital', 'Dr.ARUN', 'Dermatologist')," +
                    "('Shifa Hospital', 'Dr.Surya', 'Dentist')," +
                    "('Shifa Hospital', 'Dr.Rajesh', 'Cardiologist')," +
                    "('Shifa Hospital', 'Dr.Yusuf', 'Neurologist')," +
                    "('Shifa Hospital', 'Dr.Anitha', 'Dentist')," +
                    "('Shifa Hospital', 'Dr.Reddy', 'Psychiatrist')," +
                    "('Shifa Hospital', 'Dr.Imthiyas', 'Opthamologist')," +
                    "('Shifa Hospital', 'Dr.Ramya', 'Urologist')," +
                    "('Shifa Hospital', 'Dr.Priya', 'General Physician')," +
                    "('Shifa Hospital', 'Dr.Rekha', 'ENT Specialist')," +
                    "('Shifa Hospital', 'Dr.Punitha', 'General Physician')," +
                    "('Shifa Hospital', 'Dr.Abishek', 'ENT Specialist')," +
                    "('Shifa Hospital', 'Dr.Suga', 'Endocrinologist')," +
                    "('Shifa Hospital', 'Dr.August D', 'Dermatologist')," +
                    "('Shifa Hospital', 'Dr.Inshaaf', 'General Physician')," +
                    "('Shifa Hospital', 'Dr.Sameer', 'Opthamologist')," +
                    "('Shifa Hospital', 'Dr.Gopal', 'Gastroenterologist')," +
                    "('Shifa Hospital', 'Dr.Vaishalini', 'Cardiologist')," +
                    "('Shifa Hospital', 'Dr.Nithya', 'Dentist')," +
                    "('Shifa Hospital', 'Dr.Barakath', 'Pulmonologist')," +
                    "('Lifeline Hospital', 'Dr.Keerthi', 'Dentist')," +
                    "('Lifeline Hospital', 'Dr.Roshan', 'Psychiatrist')," +
                    "('Lifeline Hospital', 'Dr.Harini', 'Cardiologist')," +
                    "('Lifeline Hospital', 'Dr.Sarfraz', 'Urologist')," +
                    "('Lifeline Hospital', 'Dr.Azees', 'Neurologist')," +
                    "('Lifeline Hospital', 'Dr.Ishaq', 'Dermatologist')," +
                    "('Lifeline Hospital', 'Dr.Sushmitha', 'ENT Specialist')," +
                    "('Lifeline Hospital', 'Dr.Sowmiya', 'General Physician')," +
                    "('Lifeline Hospital', 'Dr.Kavitha', 'Pulmonologist')," +
                    "('Lifeline Hospital', 'Dr.Karthika', 'Dentist')," +
                    "('Lifeline Hospital', 'Dr.IshaPrathan', 'Endocrinologist')," +
                    "('Lifeline Hospital', 'Dr.Safa', 'Psychiatrist')," +
                    "('Lifeline Hospital', 'Dr.Sathiyan', 'Opthamologist')," +
                    "('Lifeline Hospital', 'Dr.Nazeer', 'Rheumatologist')," +
                    "('Lifeline Hospital', 'Dr.Asokan', 'General Physician')," +
                    "('Lifeline Hospital', 'Dr.Abishek', 'Neurologist')," +
                    "('Lifeline Hospital', 'Dr.Indhu', 'Dermatologist')," +
                    "('Lifeline Hospital', 'Dr.Gopal', 'Dentist')," +
                    "('Lifeline Hospital', 'Dr.Yusuf', 'Cardiologist')," +
                    "('Lifeline Hospital', 'Dr.IshaPrathan', 'ENT Specialist')," +
                    "('Lifeline Hospital', 'Dr.Santhosh', 'Urologist')," +
                    "('Health Harbor', 'Dr.Nivetha', 'Psychiatrist')," +
                    "('Health Harbor', 'Dr.ARUN', 'Cardiologist')," +
                    "('Health Harbor', 'Dr.Surya', 'Dermatologist')," +
                    "('Health Harbor', 'Dr.Rajesh', 'Dentist')," +
                    "('Health Harbor', 'Dr.Yusuf', 'Opthamologist')," +
                    "('Health Harbor', 'Dr.Anitha', 'Neurologist')," +
                    "('Health Harbor', 'Dr.Reddy', 'Dentist')," +
                    "('Health Harbor', 'Dr.Imthiyas', 'General Physician')," +
                    "('Health Harbor', 'Dr.Ramya', 'ENT Specialist')," +
                    "('Health Harbor', 'Dr.Priya', 'Urologist')," +
                    "('Health Harbor', 'Dr.Rekha', 'Endocrinologist')," +
                    "('Health Harbor', 'Dr.Punitha', 'Gastroenterologist')," +
                    "('Health Harbor', 'Dr.Abishek', 'Neurologist')," +
                    "('Health Harbor', 'Dr.Naseefa', 'Psychiatrist')," +
                    "('Health Harbor', 'Dr.Suga', 'General Physician')," +
                    "('Health Harbor', 'Dr.August D', 'Dermatologist')," +
                    "('Health Harbor', 'Dr.Inshaaf', 'Cardiologist')," +
                    "('Health Harbor', 'Dr.Sameer', 'ENT Specialist')," +
                    "('Health Harbor', 'Dr.Gopal', 'Pulmonologist')," +
                    "('Health Harbor', 'Dr.Vaishalini', 'Rheumatologist')," +
                    "('Healing Hub', 'Dr.Nithya', 'Cardiologist')," +
                    "('Healing Hub', 'Dr.Barakath', 'ENT Specialist')," +
                    "('Healing Hub', 'Dr.Keerthi', 'Neurologist')," +
                    "('Healing Hub', 'Dr.Harini', 'Neurologist')," +
                    "('Healing Hub', 'Dr.Sarfraz', 'General Physician')," +
                    "('Healing Hub', 'Dr.Azees', 'Dermatologist')," +
                    "('Healing Hub', 'Dr.Ishaq', 'Cardiologist')," +
                    "('Healing Hub', 'Dr.Sushmitha', 'Dentist')," +
                    "('Healing Hub', 'Dr.Shalini', 'Rheumatologist')," +
                    "('Healing Hub', 'Dr.Jimin', 'General Physician')," +
                    "('Healing Hub', 'Dr.Ramachandran', 'Dermatologist')," +
                    "('Healing Hub', 'Dr.Ezhil', 'Dentist')," +
                    "('Healing Hub', 'Dr.Shyamala', 'Pulmonologist')," +
                    "('Healing Hub', 'Dr.Sujatha', 'Opthamologist')," +
                    "('Healing Hub', 'Dr.Sowmiya', 'Endocrinologist')," +
                    "('Healing Hub', 'Dr.Kavitha', 'ENT Specialist')," +
                    "('Healing Hub', 'Dr.Karthika', 'Rheumatologist')," +
                    "('Healing Hub', 'Dr.Safa', 'Pulmonologist')," +
                    "('Healing Hub', 'Dr.Sathiyan', 'Gastroenterologist')," +
                    "('Healing Hub', 'Dr.Nazeer', 'Opthamologist')");
        }

        cursor = db.rawQuery("SELECT COUNT(*) FROM time_table", null);
        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            db.execSQL("INSERT INTO time_table (specialist, time_slot) VALUES" +
                    "('Cardiologist', '9:00 AM')," +
                    "('Cardiologist', '10:30 AM')," +
                    "('Cardiologist', '11:30 AM')," +
                    "('Cardiologist', '2:00 PM')," +
                    "('Cardiologist', '3:30 PM')," +
                    "('Cardiologist', '5:00 PM')," +
                    "('Neurologist', '9:00 AM')," +
                    "('Neurologist', '10:30 AM')," +
                    "('Neurologist', '11:30 AM')," +
                    "('Neurologist', '2:00 PM')," +
                    "('Neurologist', '3:30 PM')," +
                    "('Neurologist', '5:00 PM')," +
                    "('Endocrinologist', '9:00 AM')," +
                    "('Endocrinologist', '10:30 AM')," +
                    "('Endocrinologist', '11:30 AM')," +
                    "('Endocrinologist', '2:00 PM')," +
                    "('Endocrinologist', '3:30 PM')," +
                    "('Endocrinologist', '5:00 PM')," +
                    "('Pulmonologist', '9:00 AM')," +
                    "('Pulmonologist', '10:30 AM')," +
                    "('Pulmonologist', '11:30 AM')," +
                    "('Pulmonologist', '2:00 PM')," +
                    "('Pulmonologist', '3:30 PM')," +
                    "('Pulmonologist', '5:00 PM')," +
                    "('Dentist', '10:00 AM')," +
                    "('Dentist', '11:00 AM')," +
                    "('Dentist', '3:00 PM')," +
                    "('Dentist', '4:00 PM')," +
                    "('Dentist', '6:30 PM')," +
                    "('Dermatologist', '10:00 AM')," +
                    "('Dermatologist', '11:00 AM')," +
                    "('Dermatologist', '3:00 PM')," +
                    "('Dermatologist', '4:00 PM')," +
                    "('Dermatologist', '6:30 PM')," +
                    "('Opthamologist', '10:00 AM')," +
                    "('Opthamologist', '11:00 AM')," +
                    "('Opthamologist', '3:00 PM')," +
                    "('Opthamologist', '4:00 PM')," +
                    "('Opthamologist', '6:30 PM')," +
                    "('General Physician', '9:00 AM')," +
                    "('General Physician', '10:30 AM')," +
                    "('General Physician', '1:00 PM')," +
                    "('General Physician', '2:30 PM')," +
                    "('General Physician', '4:00 PM')," +
                    "('General Physician', '6:00 PM')," +
                    "('ENT Speacialist', '9:00 AM')," +
                    "('ENT Speacialist', '10:30 AM')," +
                    "('ENT Speacialist', '1:00 PM')," +
                    "('ENT Speacialist', '2:30 PM')," +
                    "('ENT Speacialist', '4:00 PM')," +
                    "('ENT Speacialist', '6:00 PM')," +
                    "('Psychiatrist', '10:00 AM')," +
                    "('Psychiatrist', '11:30 AM')," +
                    "('Psychiatrist', '2:00 PM')," +
                    "('Psychiatrist', '3:30 PM')," +
                    "('Psychiatrist', '5:30 PM')," +
                    "('Rheumatologist', '10:00 AM')," +
                    "('Rheumatologist', '11:30 AM')," +
                    "('Rheumatologist', '2:00 PM')," +
                    "('Rheumatologist', '3:30 PM')," +
                    "('Rheumatologist', '5:30 PM')," +
                    "('Gastroenterologist', '8:30 AM')," +
                    "('Gastroenterologist', '9:45 AM')," +
                    "('Gastroenterologist', '11:00 AM')," +
                    "('Gastroenterologist', '1:30 PM')," +
                    "('Gastroenterologist', '3:00 PM')," +
                    "('Gastroenterologist', '5:00 PM')," +
                    "('Urologist', '8:30 AM')," +
                    "('Urologist', '9:45 AM')," +
                    "('Urologist', '11:00 AM')," +
                    "('Urologist', '1:30 PM')," +
                    "('Urologist', '3:00 PM')," +
                    "('Urologist', '5:00 PM')");
        }

    }

}
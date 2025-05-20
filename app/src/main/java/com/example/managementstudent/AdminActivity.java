package com.example.managementstudent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {

    Button btnGoUser, btnGoCertificate, btnGoStudent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        init();
        goCertificate();
        goStudent();
        goUser();
    }

    public void init() {
        btnGoCertificate = findViewById(R.id.btnGoCertificate);
        btnGoUser = findViewById(R.id.btnGoUser);
        btnGoStudent = findViewById(R.id.btnGoStudent);
    }

    public void goStudent() {
        btnGoStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, StudentActivity.class);
                startActivity(intent);
            }
        });
    }

    public void goCertificate() {
        btnGoStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, CertificateActivity.class);
                startActivity(intent);
            }
        });
    }

    public void goUser() {
        btnGoStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }
}
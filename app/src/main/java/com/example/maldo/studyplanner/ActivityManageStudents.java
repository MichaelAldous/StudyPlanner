package com.example.maldo.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ActivityManageStudents extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        Button addButton = findViewById(R.id.buttonAddStudent);
        Button deleteButton = findViewById(R.id.buttonDeleteStudent);
    }
}

package com.example.maldo.studyplanner;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StudentActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

            Button plannerButton = findViewById(R.id.buttonPlanner2);
            Button addButton = findViewById(R.id.buttonAddStudent);
            Button deleteButton = findViewById(R.id.buttonDeleteStudent);

            //onClick listener for planner button
            plannerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StudentActivity.this, PlannerActivity.class);
                    startActivity(intent);
                }
        });
    }

    public void AddStudentClicked(View view){
        EditText studentID = findViewById(R.id.studentIDEditText);
        EditText fName = findViewById(R.id.studentFNameEditText);
        EditText lName = findViewById(R.id.studentLNameEditText);
        EditText email = findViewById(R.id.studentEmailEditText);

        int studentIDPass = Integer.parseInt(studentID.getText().toString());
        String fNamePass = fName.getText().toString();
        String lNamePass = lName.getText().toString();
        String emailPass = email.getText().toString();
        MyDBHandler handler = new MyDBHandler(this);
        handler.AddStudent(studentIDPass,fNamePass,lNamePass,emailPass);
    }
}

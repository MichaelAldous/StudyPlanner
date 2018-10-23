package com.example.maldo.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StudentActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

            Button plannerButton = findViewById(R.id.buttonPlanner2);
            Button addButton = findViewById(R.id.buttonAddStudent);
            Button deleteButton = findViewById(R.id.buttonDeleteStudent);


            //onClick listener for cake button, passes cake recipes to new intent
            plannerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StudentActivity.this, PlannerActivity.class);
                    startActivity(intent);
                }
        });
    }
}

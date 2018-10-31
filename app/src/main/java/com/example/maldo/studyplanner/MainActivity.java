package com.example.maldo.studyplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button plannerButton = findViewById(R.id.buttonPlanner);
        Button manageStudentButton = findViewById(R.id.buttonManageStudent);
        Button adminButton = findViewById(R.id.buttonAdmin);
        Button aboutButton = findViewById(R.id.buttonAbout);


        //onClick listener for cake button, passes cake recipes to new intent
        plannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivitySelectStudent.class);
                startActivity(intent);
            }
        });

        //onClick listener for cake button, passes cake recipes to new intent
        manageStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityManageStudents.class);
                startActivity(intent);
            }
        });

        //onClick listener for cake button, passes cake recipes to new intent
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityAdmin.class);
                startActivity(intent);
            }
        });

        //onClick listener for cake button, passes cake recipes to new intent
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityAbout.class);
                startActivity(intent);
            }
        });
    }
}

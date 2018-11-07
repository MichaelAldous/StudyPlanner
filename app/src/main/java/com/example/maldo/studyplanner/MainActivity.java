package com.example.maldo.studyplanner;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button plannerButton = findViewById(R.id.buttonPlanner);
        Button manageStudentButton = findViewById(R.id.buttonManageStudent);
        Button adminButton = findViewById(R.id.buttonAdmin);
        Button aboutButton = findViewById(R.id.buttonAbout);

        MyDBHandler dbHandler = new MyDBHandler(this);
        if(!checkDB(this, "planner.db")){
            Log.d("checkDB", "onCreate: DB DOES NOT EXIST");
            dbHandler.getWritableDatabase();
            dbHandler.PopulateDB();
            //Create database
            //dbHandler = new MyDBHandler(this);

        } else {
            Log.d("checkDB", "onCreate: DB DOES EXIST");
            //dbHandler.getWritableDatabase();
            //Check DB versions
        }

        //onClick listener for PLANNER button
        plannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivitySelectStudent.class);
                startActivity(intent);
            }
        });

        //onClick listener for MANAGE STUDENT button
        manageStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityManageStudents.class);
                startActivity(intent);
            }
        });

        //onClick listener for ADMIN button
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityAdmin.class);
                startActivity(intent);
            }
        });

        //onClick listener for ABOUT button
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityAbout.class);
                startActivity(intent);
            }
        });
    }

    private static boolean checkDB(Context context, String dbName){
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}

package com.example.maldo.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivitySelectStudent extends AppCompatActivity{

    MyDBHandler dbHandler = new MyDBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);

        Button plannerButton = findViewById(R.id.buttonPlanner2);
        final Button refreshButton = findViewById(R.id.refreshButton);

        final EditText studID = (EditText) findViewById(R.id.studentIDEditText);
        //this.refreshStudentList(Integer.parseInt(studID.getText().toString()));

        //onClick listener for planner button
        plannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySelectStudent.this, ActivityPlanner.class);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this.refreshList(Integer.parseInt(studID.getText().toString()));
                try{
                    refreshStudentList(Integer.parseInt(studID.getText().toString()));
                } catch(Exception e) {
                    Log.d("REFRESH", "onClick: NO INTEGER  ");
                    refreshStudentList(null);
                }
            }
        });

    }

    public void refreshStudentList(Integer id){
        ListView studentListView = (ListView) findViewById(R.id.studentList);
        ArrayList<Student> studentList = dbHandler.getListOfStudents(id);
        Log.d("REFRESH", "refreshStudentList: " + studentList);
    }

}

package com.example.maldo.studyplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivitySelectStudent extends AppCompatActivity{

    MyDBHandler dbHandler = new MyDBHandler(this);
    //private ListView studentListView = findViewById(R.id.studentList);
    private EditText studID;
    public static ArrayList<Student> studentList;
    private SelectStudentAdapter selectStudentAdapter;
    private ListView studentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);

        final Button refreshButton = findViewById(R.id.refreshButton);

        studID = (EditText) findViewById(R.id.studentIDEditText);
        studentListView = (ListView) findViewById(R.id.studentList);
        studentList = dbHandler.getListOfStudents(null);
        refreshStudentList(null);



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

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                Intent intent = new Intent(ActivitySelectStudent.this, ActivityPlanner.class);
                Student student = (Student) parent.getAdapter().getItem(position);
                intent.putExtra("extrasID", student.getStudentId().toString());
                intent.putExtra("extrasFname", student.getStudentFName());
                intent.putExtra("extrasLname", student.getStudentLName());
                intent.putExtra("extrasEmail", student.getStudentEmail());




                startActivity(intent);
            }
        });
    }

    public void refreshStudentList(Integer id){
        studentList = dbHandler.getListOfStudents(id);
        selectStudentAdapter = new SelectStudentAdapter(this, studentList);
        studentListView.setAdapter(selectStudentAdapter);
        Log.d("REFRESH", "refreshStudentList: " + studentList);

        selectStudentAdapter.notifyDataSetChanged();

        if(studentListView.getAdapter().getCount() == 0){
            Toast.makeText(getApplicationContext(), "No students found",
                    Toast.LENGTH_LONG).show();
        }


        Log.d("REFRESH", "refreshStudentList: " + studentList);
    }

}

package com.example.maldo.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ActivityPlanner extends AppCompatActivity {

    MyDBHandler dbHandler = new MyDBHandler(this);
    ArrayList<Module> moduleList = new ArrayList<>();
    private ListView moduleListView ;
    private PlannerModuleAdapter plannerModuleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        moduleListView = findViewById(R.id.moduleListView);
        Intent intent = getIntent();
        String studID = intent.getExtras().getString("extrasID");
        String studFname = intent.getExtras().getString("extrasFname");
        String studLname = intent.getExtras().getString("extrasLname");
        String studEmail = intent.getExtras().getString("extrasEmail");

        Spinner pathwaySpinner = findViewById(R.id.spinner_pathways);
        ArrayList<String> pathwayList = dbHandler.GetPathways();
        ArrayAdapter<String> pathwayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pathwayList);
        pathwaySpinner.setAdapter(pathwayAdapter);

        Log.d("REFRESH", "onClick: GETPATHWAYS " + pathwayList);
        Spinner semesterSpinner = findViewById(R.id.spinner_semester);
        ArrayList<Integer> semesterList = dbHandler.GetSemesters();
        ArrayAdapter<Integer> semesterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesterList);
        semesterSpinner.setAdapter(semesterAdapter);

        Log.d("REFRESH", "onClick: studID " + studID);
        refreshModuleList(Integer.parseInt(studID));

    }

    public void refreshModuleList(Integer id){
        moduleList = dbHandler.GetStudentModules(id);
        plannerModuleAdapter = new PlannerModuleAdapter(this, moduleList);
        moduleListView.setAdapter(plannerModuleAdapter);
        Log.d("REFRESH", "refreshStudentList: " + moduleList);

        plannerModuleAdapter.notifyDataSetChanged();

        if(moduleListView.getAdapter().getCount() == 0){
            Toast.makeText(getApplicationContext(), "No students found",
                    Toast.LENGTH_LONG).show();
        }


        Log.d("REFRESH", "refreshStudentList: " + moduleList);
    }
}

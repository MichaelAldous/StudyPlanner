package com.example.maldo.studyplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//TODO: Make background colour of modules update based on status and prerequirements status

public class ActivityPlanner extends AppCompatActivity {

    MyDBHandler dbHandler = new MyDBHandler(this);
    public static ArrayList<Module> moduleList = new ArrayList<>();
    public static ArrayList<Module> plannerList = new ArrayList<>();
    private ListView moduleListView ;
    private PlannerModuleAdapter plannerModuleAdapter;
    private String pathway;
    private Integer semester;
    private Boolean firstloadP = true;
    private Boolean firstloadS = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        moduleListView = findViewById(R.id.moduleListView);
        Intent intent = getIntent();
        Integer studID = Integer.parseInt(intent.getExtras().getString("extrasID"));
        String studFname = intent.getExtras().getString("extrasFname");
        String studLname = intent.getExtras().getString("extrasLname");
        String studEmail = intent.getExtras().getString("extrasEmail");

        final Spinner pathwaySpinner = findViewById(R.id.spinner_pathways);
        ArrayList<String> pathwayList = dbHandler.GetPathways();
        ArrayAdapter<String> pathwayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pathwayList);
        pathwaySpinner.setAdapter(pathwayAdapter);

        Log.d("REFRESH", "onClick: GETPATHWAYS " + pathwayList);
        final Spinner semesterSpinner = findViewById(R.id.spinner_semester);
        ArrayList<Integer> semesterList = dbHandler.GetSemesters();
        ArrayAdapter<Integer> semesterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesterList);
        semesterSpinner.setAdapter(semesterAdapter);

        Log.d("REFRESH", "onClick: studID " + studID);

        moduleList = dbHandler.GetStudentModules(studID);
        pathway = pathwaySpinner.getSelectedItem().toString();
        semester = Integer.parseInt(semesterSpinner.getSelectedItem().toString());
        Log.d("COLOUR", "ON CREATE: ");


        // Pathway selector
        pathwaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pathway = pathwaySpinner.getSelectedItem().toString();
                if(!firstloadP){
                    refreshModuleList(pathway, semester);
                }else {
                    firstloadP = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Semester selector
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                semester = Integer.parseInt(semesterSpinner.getSelectedItem().toString());
                if(!firstloadS){
                    refreshModuleList(pathway, semester);
                } else {
                    firstloadS = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Hold down to set module to passed or nyp
        //TODO: update database, set background colours, passed = green, active = white
        moduleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Module module = (Module) parent.getAdapter().getItem(position);
                if(!requirementsMet(module)){
                    module.setModuleStatus("rnm");
                    Toast.makeText(getApplicationContext(), "Prerequisites not met.",
                            Toast.LENGTH_SHORT).show();
                } else if (module.getModuleStatus().matches("nyp")) {
                    module.setModuleStatus("passed");
                    Toast.makeText(getApplicationContext(), "Module set to passed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    module.setModuleStatus("nyp");
                    Toast.makeText(getApplicationContext(), "Module set to active.",
                            Toast.LENGTH_SHORT).show();
                }
                refreshModuleList(pathway, semester);
                return false;
            }
        });

        refreshModuleList(pathway, semester);
    }

    public void refreshModuleList(String pathway, Integer semester){
        plannerList.clear();
        for(Module m: moduleList ){
            if(m.getModuleSemester().equals(semester) && (m.getPathways().contains(pathway) || m.getPathways().contains("Core"))){
                plannerList.add(m);
            }
        }
        plannerModuleAdapter = new PlannerModuleAdapter(this, plannerList);
        moduleListView.setAdapter(plannerModuleAdapter);
        Log.d("PARENT", "refreshStudentList: " + plannerList);
        plannerModuleAdapter.notifyDataSetChanged();

        if(moduleListView.getAdapter().getCount() == 0){
            Toast.makeText(getApplicationContext(), "Error, no modules found. Please recreate student.",
                    Toast.LENGTH_LONG).show();
        }
    }

     // Used for onItemLongClick, to check if requirements met, before changing Module to passed
    public boolean requirementsMet(Module module){
        boolean isMet = false;
        ArrayList<String> prereqs = module.getModulePrereqs();
        if(prereqs.size()==0){
            isMet = true;
        } else {
            for(String required: prereqs){
                Log.d("REFRESH", "required: "+ required);
                Module requiredModule = findMod(required);
                Log.d("REFRESH", "requiredModule: "+ requiredModule);
                if(requiredModule.getModuleStatus().equals("passed")){
                    isMet = true;
                    Log.d("REFRESH", "If module shit = passed: ");
                } else {
                    Log.d("REFRESH", "If module shit = npy: ");
                    isMet = false;
                    return isMet;
                }
            }
        }
        //if no requirements isMet = true
        return isMet;
    }

    // Used by requirementsMet to get Module object
    private Module findMod(String modID){
        for(Module mod: moduleList){
            if(mod.getModuleId().equals(modID)){
                return mod;
            }
        }
        return null;
    }

}

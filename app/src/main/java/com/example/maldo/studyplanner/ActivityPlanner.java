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
    private Integer studID;
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
        studID = Integer.parseInt(intent.getExtras().getString("extrasID"));
        final String studFname = intent.getExtras().getString("extrasFname");
        final String studLname = intent.getExtras().getString("extrasLname");
        final String studEmail = intent.getExtras().getString("extrasEmail");

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
        moduleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Module module = (Module) parent.getAdapter().getItem(position);
                if(!requirementsMet(module)){
                    module.setModuleStatus("rnm");
                    Toast.makeText(getApplicationContext(), "Prerequisites not met.",
                            Toast.LENGTH_SHORT).show();
                } else if (module.getModuleStatus().matches("active")) {
                    module.setModuleStatus("passed");
                    Toast.makeText(getApplicationContext(), "Module set to passed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    module.setModuleStatus("active");
                    Toast.makeText(getApplicationContext(), "Module set to active.",
                            Toast.LENGTH_SHORT).show();
                }
                updateModulePrereqStatus(module.getModuleId());
                dbHandler.updateModuleStatus(studID, module.getModuleId(), module.getModuleStatus());
                refreshModuleList(pathway, semester);
                return false;
            }
        });

        refreshModuleList(pathway, semester);

        Button sendEmailButton = (Button) findViewById(R.id.buttonEmail);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String emailSender = "degreeprogrammemapper@gmail.com";
                            String emailPW = "OursIsTheFury";
                            String studName = studFname + " " + studLname;
                            String message = compileMessage(pathway, studName);

                            GMailSender sender = new GMailSender(
                                    emailSender,
                                    emailPW);
                            //sender.addAttachment();
                            sender.sendMail("Test mail", message,
                                    emailSender,
                                    studEmail);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();                        }
                    }
                }).start();
                Toast.makeText(getApplicationContext(), "Email Sent to " + studEmail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String compileMessage(String pathway, String studName){
        String message =  "Degree Mapper for " + studName + " \n" +
                            "Pathway: " + pathway + " \n";
        for(int i = 1; i < 7; i++){
            message = message + "------------------------ \n" +
                                "Semester: " + i + " \n"
                                + "------------------------ \n";
            for(Module mod: moduleList){
                if((mod.getPathways().contains(pathway) || mod.getPathways().contains("Core")) && mod.getModuleSemester().equals(i)){
                    if(mod.getModuleStatus().equals("rnm")){
                        message = message + mod.getModuleId() + ", Requirements Not Met \n";
                    } else if (mod.getModuleStatus().equals("active")){
                        message = message + mod.getModuleId() + ", Active \n";
                    } else {
                        message = message + mod.getModuleId() + ", Passed \n";
                    }
                }
            }
        }

        return message;
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

    public void updateModulePrereqStatus(String upDatedMod){
        for(Module mod: moduleList){
            if(mod.getModulePrereqs().contains(upDatedMod)){
                Boolean allPrereqPassed = true;
                for(String prereqMod: mod.getModulePrereqs()){
                    if(findMod(prereqMod).getModuleStatus().equals("active") ||findMod(prereqMod).getModuleStatus().equals("rnm")){
                        allPrereqPassed = false;
                        break;
                    }
                }
                Log.d("UPDATEMOD", "updateModulePrereqStatus MODULE: " + mod.getModuleId());
                if(allPrereqPassed){
                    mod.setModuleStatus("active");
                    dbHandler.updateModuleStatus(studID, mod.getModuleId(), mod.getModuleStatus());
                    Log.d("UPDATEMOD", "updateModulePrereqStatus ACTIVE: " + mod.getModuleStatus());
                } else {
                    mod.setModuleStatus("rnm");
                    dbHandler.updateModuleStatus(studID, mod.getModuleId(), mod.getModuleStatus());
                    Log.d("UPDATEMOD", "updateModulePrereqStatus RNM: " + mod.getModuleStatus());
                }
            }
        }
    }

}

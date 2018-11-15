package com.example.maldo.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityEditor extends AppCompatActivity {
        MyDBHandler dbHandler = new MyDBHandler(this);
    public static ArrayList<Module> moduleList = new ArrayList<>();
    public static ArrayList<Module> plannerList = new ArrayList<>();
    private ListView moduleListView ;
    private EditorModuleAdapter editorModuleAdapter;
    private String pathway;
    private Integer semester;
    private Boolean firstloadP = true;
    private Boolean firstloadS = true;
    private Boolean firstloadRB = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        moduleListView = findViewById(R.id.editor_moduleListView);

        final Spinner pathwaySpinner = findViewById(R.id.editor_spinner_pathways);
        ArrayList<String> pathwayList = dbHandler.GetPathways();
        ArrayAdapter<String> pathwayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pathwayList);
        pathwaySpinner.setAdapter(pathwayAdapter);

        final Spinner semesterSpinner = findViewById(R.id.editor_spinner_semester);
        ArrayList<Integer> semesterList = dbHandler.GetSemesters();
        ArrayAdapter<Integer> semesterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesterList);
        semesterSpinner.setAdapter(semesterAdapter);

        moduleList = dbHandler.GetEditorModules();
        pathway = pathwaySpinner.getSelectedItem().toString();
        semester = Integer.parseInt(semesterSpinner.getSelectedItem().toString());


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

        Button resetButton = (Button) findViewById(R.id.editor_reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!firstloadRB) {
                    dbHandler.createEditorDB();
                    refreshModuleList(pathway, semester);
                } else {
                    firstloadRB = false;
                }
            }
        });


        Button sendEmailButton = (Button) findViewById(R.id.editor_buttonEmail);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String emailSender = "degreeprogrammemapper@gmail.com";
                            String emailPW = "OursIsTheFury";
                            String message = compileMessage(pathway);

                            GMailSender sender = new GMailSender(
                                    emailSender,
                                    emailPW);
                            //sender.addAttachment();
                            sender.sendMail("Test mail", message,
                                    emailSender,
                                    emailSender);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();                        }
                    }
                }).start();
            }
        });

        refreshModuleList(pathway, semester);
    }

    private String compileMessage(String pathway){
        String message =  "Degree Mapper for " + "" + " \n" +
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
        editorModuleAdapter = new EditorModuleAdapter(this, plannerList);
        moduleListView.setAdapter(editorModuleAdapter);
        Log.d("PARENT", "refreshStudentList: " + plannerList);
        Log.d("PARENT", "refreshStudentList: " + moduleList);
        editorModuleAdapter.notifyDataSetChanged();

        if(moduleListView.getAdapter().getCount() == 0){
            Toast.makeText(getApplicationContext(), "Error, no modules found. Please recreate student.",
                    Toast.LENGTH_LONG).show();
        }
    }
}

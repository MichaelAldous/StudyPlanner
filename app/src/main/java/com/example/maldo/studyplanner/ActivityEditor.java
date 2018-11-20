package com.example.maldo.studyplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityEditor extends AppCompatActivity {
    MyDBHandler dbHandler = new MyDBHandler(this);
    private static ArrayList<Module> moduleList = new ArrayList<>();
    private static ArrayList<Module> editorList = new ArrayList<>();
    private static ArrayList<String> pathwayList = new ArrayList<>();
    private static ArrayAdapter<String> moduleAdapter;// = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modIdList);
    private static ArrayList<String> modSpinnerList = new ArrayList<>();
    private String moduleID;
    private String pathway;
    private Integer semester;
    private Boolean firstloadRB = true;
    private Spinner modulesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        modulesSpinner = findViewById(R.id.editor_spinner_modules);
        moduleList = dbHandler.GetEditorModules();

        this.updateModuleSpinner();

        final EditText etModId = (EditText) findViewById(R.id.et_modId);
        final EditText etModName = (EditText) findViewById(R.id.et_modName);
        final EditText etModCredits = (EditText) findViewById(R.id.et_credits);
        final Spinner spinner_semester = (Spinner) findViewById(R.id.spinner_semester);
        final Spinner spinner_pathway = (Spinner) findViewById(R.id.spinner_pathways);
        final Spinner spinner_prereq = (Spinner) findViewById(R.id.spinner_prereq);

        ArrayList<Integer> semesterList = dbHandler.GetSemesters();
        ArrayAdapter<Integer> semesterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesterList);
        spinner_semester.setAdapter(semesterAdapter);



        // Module selector
        // Loads module data into editText and spinners
        modulesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //moduleID = modulesSpinner.getSelectedItem().toString();
                //refreshModuleList(pathway, semester);
                if(moduleList.contains(findMod(modulesSpinner.getSelectedItem().toString()))){
                    Module selectedMod = findMod(modulesSpinner.getSelectedItem().toString());
                    etModId.setText(selectedMod.getModuleId());
                    etModName.setText(selectedMod.getModuleName());
                    etModCredits.setText(""+selectedMod.getModuleCredits());
                    spinner_semester.setSelection(selectedMod.getModuleSemester()-1);

                    //Pathway spinner
                    ArrayList<StateVO> listVOs = new ArrayList<>();
                    StateVO titleStateVO = new StateVO();
                    titleStateVO.setTitle("Select Pathways");
                    listVOs.add(titleStateVO);
                    pathwayList = dbHandler.EditorGetPathways();
                    for(int j = 0; j < pathwayList.size(); j++){
                        StateVO stateVO = new StateVO();
                        stateVO.setTitle(pathwayList.get(j));
                        stateVO.setSelected(modInPathway(pathwayList.get(j), selectedMod.getModuleId()));
                        listVOs.add(stateVO);
                    }
                    MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(ActivityEditor.this, 0, listVOs);
                    spinner_pathway.setAdapter(mySpinnerAdapter);

                    //Prerequisite spinner
                    listVOs = new ArrayList<>();
                    titleStateVO = new StateVO();
                    titleStateVO.setTitle("Select Prerequisites");
                    listVOs.add(titleStateVO);
                    for(Module mod: moduleList){
                        StateVO stateVO = new StateVO();
                        stateVO.setTitle(mod.getModuleId());
                        stateVO.setSelected(mod.getModulePrereqs().contains(selectedMod.getModuleId()));
                    }
                    mySpinnerAdapter = new MySpinnerAdapter(ActivityEditor.this, 0, listVOs);
                    spinner_prereq.setAdapter(mySpinnerAdapter);



                } else {

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
                    //refreshModuleList(pathway, semester);
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
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();}
                    }
                }).start();
            }
        });

        Button saveButton = (Button) findViewById(R.id.editor_buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public boolean modInPathway(String pathway, String moduleID){
        Module mod = findMod(moduleID);
        for(String pathways: mod.getPathways()){
            if(pathways.contains(pathway)){
                return true;
            }
        }
        return false;
    }

    // Used by requirementsMet to get Module object
    public static Module findMod(String modID){
        for(Module mod: moduleList){
            if(mod.getModuleId().equals(modID)){
                return mod;
            }
        }
        return null;
    }

    public void updateModuleSpinner(){
        for(Module mod: moduleList){
            modSpinnerList.add(mod.getModuleId());
        }
        moduleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modSpinnerList);
        modulesSpinner.setAdapter(moduleAdapter);
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

    public void refreshModuleList(){
        editorList.clear();
        for(Module m: moduleList ){
            editorList.add(m);
            if(m.getModuleSemester().equals(semester) && (m.getPathways().contains(pathway) || m.getPathways().contains("Core"))){
            }
        }
        //editorModuleAdapter = new EditorModuleAdapter(this, editorList);
        //moduleListView.setAdapter(editorModuleAdapter);
        Log.d("PARENT", "refreshStudentList: " + editorList);
        Log.d("PARENT", "refreshStudentList: " + moduleList);
        //editorModuleAdapter.notifyDataSetChanged();

        //if(moduleListView.getAdapter().getCount() == 0){
        //    Toast.makeText(getApplicationContext(), "Error, no modules found. Please recreate student.",
        //            Toast.LENGTH_LONG).show();
        //}
    }
}

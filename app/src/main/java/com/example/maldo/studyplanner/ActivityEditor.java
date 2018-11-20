package com.example.maldo.studyplanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    private Spinner spinner_semester;
    private Spinner spinner_pathway;
    private Spinner spinner_prereq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        modulesSpinner = findViewById(R.id.editor_spinner_modules);

        this.updateModuleSpinner();
        editorList = moduleList;

        final EditText etModId = (EditText) findViewById(R.id.et_modId);
        final EditText etModName = (EditText) findViewById(R.id.et_modName);
        final EditText etModCredits = (EditText) findViewById(R.id.et_credits);
        spinner_semester = (Spinner) findViewById(R.id.spinner_semester);
        spinner_pathway = (Spinner) findViewById(R.id.spinner_pathways);
        spinner_prereq = (Spinner) findViewById(R.id.spinner_prereq);

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
                    ArrayList<StateVO> pathwayVOList = new ArrayList<>();
                    StateVO titleStateVO = new StateVO();
                    titleStateVO.setTitle("Select Pathways");
                    pathwayVOList.add(titleStateVO);
                    pathwayList = dbHandler.EditorGetPathways();
                    for(int j = 0; j < pathwayList.size(); j++){
                        StateVO stateVO = new StateVO();
                        stateVO.setTitle(pathwayList.get(j));
                        stateVO.setSelected(modInPathway(pathwayList.get(j), selectedMod.getModuleId()));
                        pathwayVOList.add(stateVO);
                    }
                    MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(ActivityEditor.this, 0, pathwayVOList);
                    spinner_pathway.setAdapter(mySpinnerAdapter);

                    //Prerequisite spinner
                    ArrayList<StateVO> prereqVOList = new ArrayList<>();
                    titleStateVO = new StateVO();
                    titleStateVO.setTitle("Select Prerequisites");
                    prereqVOList.add(titleStateVO);
                    for(Module mod: moduleList){
                        StateVO stateVO = new StateVO();
                        stateVO.setTitle(mod.getModuleId());
                        stateVO.setSelected(mod.getModulePrereqs().contains(selectedMod.getModuleId()));
                        prereqVOList.add(stateVO);
                    }
                    mySpinnerAdapter = new MySpinnerAdapter(ActivityEditor.this, 0, prereqVOList);
                    spinner_prereq.setAdapter(mySpinnerAdapter);

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
                dbHandler.createEditorDB();
                moduleList = dbHandler.GetEditorModules();
                updateModuleSpinner();
                Toast.makeText(getApplicationContext(),"Modules reset!",Toast.LENGTH_SHORT).show();
            }
        });


        Button sendEmailButton = (Button) findViewById(R.id.editor_buttonEmail);
        final String emailSender = "degreeprogrammemapper@gmail.com";
        final String emailPW = "OursIsTheFury";
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

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
                Toast.makeText(getApplicationContext(), "Email Sent to " + emailSender, Toast.LENGTH_SHORT).show();
            }
        });

        Button saveButton = (Button) findViewById(R.id.editor_buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySpinnerAdapter pathwaySpinnerList = (MySpinnerAdapter) spinner_pathway.getAdapter();
                MySpinnerAdapter prereqSpinnerList = (MySpinnerAdapter) spinner_prereq.getAdapter();

                if(moduleList.contains(findMod(etModId.getText().toString()))){
                    Module module = findMod(etModId.getText().toString());
                    module.setModName(etModName.getText().toString());
                    module.setModCredits(Integer.parseInt(etModCredits.getText().toString()));
                    module.setModSemester(Integer.parseInt(spinner_semester.getSelectedItem().toString()));
                    ArrayList<String> pathways = new ArrayList<>();
                    for(int i = 1; i < pathwaySpinnerList.getCount(); i++){
                        if(pathwaySpinnerList.getItem(i).isSelected()){
                            pathways.add(pathwaySpinnerList.getItem(i).getTitle());
                        }
                    }
                    module.setModPathways(pathways);
                    ArrayList<String> prereq = new ArrayList<>();
                    for(int i = 1; i < prereqSpinnerList.getCount(); i++){
                        if(prereqSpinnerList.getItem(i).isSelected()){
                            prereq.add(prereqSpinnerList.getItem(i).getTitle());
                        }
                    }
                    module.setModPrereqs(prereq);
                    dbHandler.EditorUpdateModules(module);
                    Toast.makeText(getApplicationContext(),"Module "+ module.getModuleId() +" updated!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Module not found, add first!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button addButton = (Button) findViewById(R.id.editor_buttonAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySpinnerAdapter pathwaySpinnerList = (MySpinnerAdapter) spinner_pathway.getAdapter();
                MySpinnerAdapter prereqSpinnerList = (MySpinnerAdapter) spinner_prereq.getAdapter();

                ArrayList<String> pathways = new ArrayList<>();
                for(int i = 1; i < pathwaySpinnerList.getCount(); i++){
                    if(pathwaySpinnerList.getItem(i).isSelected()){
                        pathways.add(pathwaySpinnerList.getItem(i).getTitle());
                    }
                }
                ArrayList<String> prereq = new ArrayList<>();
                for(int i = 1; i < prereqSpinnerList.getCount(); i++){
                    if(prereqSpinnerList.getItem(i).isSelected()){
                        prereq.add(prereqSpinnerList.getItem(i).getTitle());
                    }
                }
                if(!moduleList.contains(findMod(etModId.getText().toString()))){
                    Module module = new Module(
                            etModId.getText().toString(),
                            etModName.getText().toString(),
                            "",
                            prereq,
                            pathways,
                            Integer.parseInt(spinner_semester.getSelectedItem().toString()),
                            Integer.parseInt(etModCredits.getText().toString()),
                            "active");
                    moduleList.add(module);
                    dbHandler.EditorAddModule(module);
                    updateModuleSpinner();
                    Toast.makeText(getApplicationContext(),"Module "+  etModId.getText().toString() +" added!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Module already exists!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button deleteButton = (Button) findViewById(R.id.editor_buttonDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moduleList.contains(findMod(etModId.getText().toString()))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditor.this);
                    builder.setCancelable(true);
                    builder.setTitle("Remove Module?");
                    builder.setMessage("Are you sure you want to remove module: " + etModId.getText().toString() + "?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which){
                                    Module module = findMod(etModId.getText().toString());
                                    moduleList.remove(module.getModuleId());
                                    dbHandler.EditorDeleteModule(module);
                                    updateModuleSpinner();
                                    Toast.makeText(getApplicationContext(),"Module removed!",Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialogInterface, int which){
                            Toast.makeText(getApplicationContext(), "Cancelled",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    Toast.makeText(getApplicationContext(),"Module not found!",Toast.LENGTH_SHORT).show();
                }
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
        moduleList.clear();
        modSpinnerList.clear();
        moduleList = dbHandler.GetEditorModules();
        for(Module mod: moduleList){
            modSpinnerList.add(mod.getModuleId());
        }
        moduleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modSpinnerList);
        modulesSpinner.setAdapter(moduleAdapter);
    }

    private String compileMessage(String pathway){
        String message =  "Degree Mapper: " + " \n";
        for(int i = 1; i < 7; i++){
            message = message + "------------------------ \n" +
                    "Semester: " + i + " \n"
                    + "------------------------ \n";
            for(Module mod: moduleList){
                if(mod.getModuleSemester().equals(i)){
                    message = message + "Mod Id: " + mod.getModuleId() + ", Name: " + mod.getModuleName() + ", Semester: " + mod.getModuleSemester() + "\n" +
                    "Pathways: " + mod.getPathways() + ", Prerequisites: " + mod.getModulePrereqs() + "\n\n";
                }
            }
        }

        return message;
    }
}

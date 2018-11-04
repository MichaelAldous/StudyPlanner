package com.example.maldo.studyplanner;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.maldo.studyplanner.Data.*;

import java.util.ArrayList;
import java.util.Arrays;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "planner.db";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        // creating the pathways table
        final String SQL_CREATE_PATHWAYS_TABLE = "CREATE TABLE " +
                pathwaysEntry.TABLE_PATHWAYS + "( " +
                pathwaysEntry.COLUMN_PATH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                pathwaysEntry.COLUMN_PATH_NAME+ " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_PATHWAYS_TABLE);

        // creating the modules table
        final String SQL_CREATE_MODULES_TABLE = "CREATE TABLE " +
                modulesEntry.TABLE_MODULES + "( " +
                modulesEntry.COLUMN_MOD_ID + " TEXT PRIMARY KEY, " +
                modulesEntry.COLUMN_MOD_NAME+ " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MOD_PREREQ + " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MP_SEMESTER + " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MOD_DESC + " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MOD_CRED + " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_MODULES_TABLE);

        // creating the mod path table
        final String SQL_CREATE_MODPATH_TABLE = "CREATE TABLE " +
                modPathsEntry.TABLE_MOD_PATH + "( " +
                modPathsEntry.COLUMN_MP_MOD_ID + " INTEGER, " +
                modPathsEntry.COLUMN_MP_PATH_ID + " INTEGER, " +
                "PRIMARY KEY("+modPathsEntry.COLUMN_MP_MOD_ID +","+ modPathsEntry.COLUMN_MP_PATH_ID+"), "+
                "FOREIGN KEY("+modPathsEntry.COLUMN_MP_MOD_ID +") REFERENCES "+modulesEntry.TABLE_MODULES+"("+modulesEntry.COLUMN_MOD_ID+"),"+
                "FOREIGN KEY("+modPathsEntry.COLUMN_MP_PATH_ID+") REFERENCES "+pathwaysEntry.TABLE_PATHWAYS+"("+pathwaysEntry.COLUMN_PATH_ID+"));";
        db.execSQL(SQL_CREATE_MODPATH_TABLE);

        // creating the mod preque table
        final String SQL_CREATE_MODPREREQ_TABLE = "CREATE TABLE " +
                modPrereqEntry.TABLE_MOD_PREREQ + "( " +
                modPrereqEntry.COLUMN_MP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                modPrereqEntry.COLUMN_MP_MOD_ID + " TEXT, " +
                modPrereqEntry.COLUMN_MP_PREREQ_ID + " TEXT, " +
                "FOREIGN KEY("+modPathsEntry.COLUMN_MP_MOD_ID +") REFERENCES "+modulesEntry.TABLE_MODULES+"("+modulesEntry.COLUMN_MOD_ID+"),"+
                "FOREIGN KEY("+modPathsEntry.COLUMN_MP_PATH_ID+") REFERENCES "+pathwaysEntry.TABLE_PATHWAYS+"("+pathwaysEntry.COLUMN_PATH_ID+"));";
        db.execSQL(SQL_CREATE_MODPREREQ_TABLE);

        // creating the student table
        final String SQL_CREATE_STUDENT_TABLE = "CREATE TABLE " +
                studentEntry.TABLE_STUDENTS + "( " +
                studentEntry.COLUMN_STUD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                studentEntry.COLUMN_STUD_FNAME+ " TEXT NOT NULL, " +
                studentEntry.COLUMN_STUD_LNAME+ " TEXT NOT NULL, " +
                studentEntry.COLUMN_STUD_EMAIL+ " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_STUDENT_TABLE);

        // creating the student module table
        final String SQL_CREATE_STUDENTMODULE_TABLE = "CREATE TABLE " +
                studentModuleEntry.TABLE_STUD_MOD + "( " +
                studentModuleEntry.COLUMN_SM_STUD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                studentModuleEntry.COLUMN__SM_MOD_ID + " TEXT NOT NULL, " +
                studentModuleEntry.COLUMN__SM_STATUS + " TEXT NOT NULL "  + ");";
        db.execSQL(SQL_CREATE_STUDENTMODULE_TABLE);
        this.PopulateDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // for updating the database
        db.execSQL("DROP TABLE IF EXISTS " + modPrereqEntry.TABLE_MOD_PREREQ);
        db.execSQL("DROP TABLE IF EXISTS " + modPathsEntry.TABLE_MOD_PATH);
        db.execSQL("DROP TABLE IF EXISTS " + studentModuleEntry.TABLE_STUD_MOD);
        db.execSQL("DROP TABLE IF EXISTS " + pathwaysEntry.TABLE_PATHWAYS);
        db.execSQL("DROP TABLE IF EXISTS " + modulesEntry.TABLE_MODULES);
        db.execSQL("DROP TABLE IF EXISTS " + studentEntry.TABLE_STUDENTS);
        onCreate(db);
    }

    public void AddStudent(int studID, String fName, String lName, String email){
        ContentValues values = new ContentValues();
        values.put(studentEntry.COLUMN_STUD_ID, studID);
        values.put(studentEntry.COLUMN_STUD_FNAME, fName);
        values.put(studentEntry.COLUMN_STUD_LNAME, lName);
        values.put(studentEntry.COLUMN_STUD_EMAIL, email);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(studentEntry.TABLE_STUDENTS, null, values);
        db.close();
    }

    //Remove student
    public void RemoveStudent(int studID){
        //Remove from mod_student

        //Remove student
    }
    //Search students?
    public void SearchStudents(){
        //Search student table
        //return student name + id
    }

    public void GetStudentModules(){
        //Search for student modules + returns name and if they've passed etc.
    }

    public void StudentModUpdate(){
        //Update stud_mod table if student passes module, or new one meets prerequisites
    }

    public void AddModule(SQLiteDatabase db, String modID, String modName,  int cred, int semester, String description, ArrayList<String> prereq, ArrayList<Integer> pathways){
        //Add module to module table
        ContentValues values = new ContentValues();
        values.put(modulesEntry.COLUMN_MOD_ID, modID);
        values.put(modulesEntry.COLUMN_MOD_NAME, modName);
        values.put(modulesEntry.COLUMN_MP_SEMESTER, semester);
        values.put(modulesEntry.COLUMN_MOD_DESC, description);
        values.put(modulesEntry.COLUMN_MOD_CRED, cred);
        db.insert(modulesEntry.TABLE_MODULES, null, values);

        //Add module pre-requirements
        values = new ContentValues();
        if(prereq!=null) {
            for (String s : prereq) {
                values.put(modID, s);
            }
        }
        db.insert(modPrereqEntry.TABLE_MOD_PREREQ, null, values);

        values = new ContentValues();
        if(pathways!=null){
            for(Integer pathID : pathways){
                values.put(modID, pathID);
            }
        } else {
            values.put(modID, 5);
        }

    }

    public void AddPathways(SQLiteDatabase db, String pathwayName){
        //Add module to module table
        ContentValues values = new ContentValues();
        values.put(pathwaysEntry.COLUMN_PATH_NAME, pathwayName);
        db.insert(modulesEntry.TABLE_MODULES, null, values);
    }


    //populate
    public void PopulateDB(SQLiteDatabase db){
        //pathway(5)
        //Network Engineering
        //Software Engineering
        //Database Architecture
        //Multimedia and Web Development
        //Core == Null??
        this.AddPathways(db, "Software Engineering"); //1
        this.AddPathways(db, "Network Engineering"); //2
        this.AddPathways(db, "Database Architecture"); //3
        this.AddPathways(db, "Multimedia and Web Development"); //4
        this.AddPathways(db, "Core");


        //Modules()
        //Database, ID, Name, ArrayList<String> Pre-reqs, credits, semester, description, ArrayList<Integer> pathways
        //this.AddModule(db, "", "",null,15, 2,"Info501 desc", null);
        ArrayList<String> prequeList = new ArrayList<>(Arrays.asList(""));
        ArrayList<Integer> pathwayList = new ArrayList<>();

        //Semester 1
        this.AddModule(db, "COMP501", "IT Operations",15, 1,"COMP501 desc",prequeList, null);
        this.AddModule(db, "COMP502", "Fundamentals of Programming and Problem Solving",15, 1,"COMP502 desc", prequeList, null);
        this.AddModule(db, "INFO501", "Professional Practice",15, 1,"INFO501 desc", prequeList, null);
        this.AddModule(db, "INFO502", "Business Systems Analysis & Design",15, 1,"INFO502 desc", prequeList, null);

        //Semester 2
        this.AddModule(db, "COMP503", "Introduction to Networks (Cisco 1)",15, 2,"COMP503 desc", prequeList, null);
        this.AddModule(db, "COMP504", "Operating Systems & Systems Support",15, 2,"COMP504 desc", prequeList, null);
        this.AddModule(db, "INFO503", "Database Principles",15, 2,"INFO503 desc", prequeList, null);
        this.AddModule(db, "INFO504", "Technical Support",15, 2,"INFO504 desc", prequeList, null);

        //Semester 3
        prequeList = new ArrayList<>(Arrays.asList("COMP502"));
        this.AddModule(db, "COMP601", "Object Oriented Programming",15, 3,"COMP601 desc",prequeList, null);
        prequeList = new ArrayList<>(Arrays.asList("INFO503"));
        this.AddModule(db, "INFO601", "Data-modelling and SQL",15, 3,"INFO601 desc",prequeList, null);
        prequeList = new ArrayList<>(Arrays.asList(""));
        this.AddModule(db, "MATH601", "Mathematics for IT",15, 3,"MATH601 desc",prequeList, null);
        prequeList = new ArrayList<>(Arrays.asList("COMP502","INFO502"));
        this.AddModule(db, "COMP602", "Web Development",15, 3,"COMP602 desc",prequeList, null);

        //Semester 4
        prequeList = new ArrayList<>(Arrays.asList(""));
        this.AddModule(db, "", "",15, 4,"Info501 desc",prequeList, null);

        //Semester 5

        //Semester 6

        //Path mods


        //Close db
        db.close();
    }





}

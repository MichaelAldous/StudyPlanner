package com.example.maldo.studyplanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maldo.studyplanner.Data.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "planner.db";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        // create PATHWAYS
        final String SQL_CREATE_PATHWAYS_TABLE = "CREATE TABLE " +
                pathwaysEntry.TABLE_PATHWAYS + "( " +
                pathwaysEntry.COLUMN_PATH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                pathwaysEntry.COLUMN_PATH_NAME+ " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_PATHWAYS_TABLE);

        // create MODULES
        final String SQL_CREATE_MODULES_TABLE = "CREATE TABLE " +
                modulesEntry.TABLE_MODULES + "( " +
                modulesEntry.COLUMN_MOD_ID + " TEXT PRIMARY KEY, " +
                modulesEntry.COLUMN_MOD_NAME+ " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MP_SEMESTER + " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MOD_DESC + " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MOD_CRED + " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_MODULES_TABLE);

        // create MODULE PATHWAYS
        final String SQL_CREATE_MODPATH_TABLE = "CREATE TABLE " +
                modPathsEntry.TABLE_MOD_PATH + "( " +
                modPathsEntry.COLUMN_MP_MOD_ID + " INTEGER, " +
                modPathsEntry.COLUMN_MP_PATH_ID + " INTEGER, " +
                "PRIMARY KEY("+modPathsEntry.COLUMN_MP_MOD_ID +","+ modPathsEntry.COLUMN_MP_PATH_ID+"), "+
                "FOREIGN KEY("+modPathsEntry.COLUMN_MP_MOD_ID +") REFERENCES "+modulesEntry.TABLE_MODULES+"("+modulesEntry.COLUMN_MOD_ID+"),"+
                "FOREIGN KEY("+modPathsEntry.COLUMN_MP_PATH_ID+") REFERENCES "+pathwaysEntry.TABLE_PATHWAYS+"("+pathwaysEntry.COLUMN_PATH_ID+"));";
        db.execSQL(SQL_CREATE_MODPATH_TABLE);

        // create MODULE PREREQUISITES
        final String SQL_CREATE_MODPREREQ_TABLE = "CREATE TABLE " +
                modPrereqEntry.TABLE_REQUIREMENTS + "( " +
                modPrereqEntry.COLUMN_REQ_MOD_ID + " TEXT, " +
                modPrereqEntry.COLUMN_REQ_REQMOD_ID + " TEXT, " +
                "PRIMARY KEY("+modPrereqEntry.COLUMN_REQ_MOD_ID +","+ modPrereqEntry.COLUMN_REQ_REQMOD_ID+"), "+
                "FOREIGN KEY("+modPrereqEntry.COLUMN_REQ_MOD_ID +") REFERENCES "+modulesEntry.TABLE_MODULES+"("+modulesEntry.COLUMN_MOD_ID+"),"+
                "FOREIGN KEY("+modPrereqEntry.COLUMN_REQ_REQMOD_ID+") REFERENCES "+modulesEntry.TABLE_MODULES+"("+modulesEntry.COLUMN_MOD_ID+"));";
        db.execSQL(SQL_CREATE_MODPREREQ_TABLE);

        // create STUDENTS
        final String SQL_CREATE_STUDENT_TABLE = "CREATE TABLE " +
                studentEntry.TABLE_STUDENTS + "( " +
                studentEntry.COLUMN_STUD_ID + " INTEGER PRIMARY KEY, " +
                studentEntry.COLUMN_STUD_FNAME+ " TEXT NOT NULL, " +
                studentEntry.COLUMN_STUD_LNAME+ " TEXT NOT NULL, " +
                studentEntry.COLUMN_STUD_EMAIL+ " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_STUDENT_TABLE);

        // create STUDENT MODULES
        final String SQL_CREATE_STUDENTMODULE_TABLE = "CREATE TABLE " +
                studentModuleEntry.TABLE_STUD_MOD + "( " +
                studentModuleEntry.COLUMN_SM_STUD_ID + " INTEGER, " +
                studentModuleEntry.COLUMN_SM_MOD_ID + " TEXT NOT NULL, " +
                studentModuleEntry.COLUMN_SM_STATUS + " TEXT NOT NULL, "  +
                "PRIMARY KEY("+studentModuleEntry.COLUMN_SM_STUD_ID +","+ studentModuleEntry.COLUMN_SM_MOD_ID+"), "+
                "FOREIGN KEY("+studentModuleEntry.COLUMN_SM_STUD_ID +") REFERENCES "+studentEntry.TABLE_STUDENTS+"("+studentEntry.COLUMN_STUD_ID+"),"+
                "FOREIGN KEY("+studentModuleEntry.COLUMN_SM_MOD_ID+") REFERENCES "+modulesEntry.TABLE_MODULES+"("+modulesEntry.COLUMN_MOD_ID+"));";
        db.execSQL(SQL_CREATE_STUDENTMODULE_TABLE);
        //this.PopulateDB(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // for updating the database
        db.execSQL("DROP TABLE IF EXISTS " + modPrereqEntry.TABLE_REQUIREMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + modPathsEntry.TABLE_MOD_PATH);
        db.execSQL("DROP TABLE IF EXISTS " + studentModuleEntry.TABLE_STUD_MOD);
        db.execSQL("DROP TABLE IF EXISTS " + pathwaysEntry.TABLE_PATHWAYS);
        db.execSQL("DROP TABLE IF EXISTS " + modulesEntry.TABLE_MODULES);
        db.execSQL("DROP TABLE IF EXISTS " + studentEntry.TABLE_STUDENTS);
        onCreate(db);
    }

    // --------------------------------- EDITOR STUFF --------------------------------- //

    // Copies tables: Module, Pathway, and mod_path for the Editor to use.
    public void createEditorDB(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + editorModPrereqEntry.TABLE_REQUIREMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + editorModPathsEntry.TABLE_MOD_PATH);
        db.execSQL("DROP TABLE IF EXISTS " + editorPathwaysEntry.TABLE_PATHWAYS);
        db.execSQL("DROP TABLE IF EXISTS " + editorModulesEntry.TABLE_MODULES);
        db.execSQL("CREATE TABLE " + editorModulesEntry.TABLE_MODULES + " AS SELECT * FROM " + modulesEntry.TABLE_MODULES + ";");
        db.execSQL("CREATE TABLE " + editorPathwaysEntry.TABLE_PATHWAYS + " AS SELECT * FROM " + pathwaysEntry.TABLE_PATHWAYS + ";");
        db.execSQL("CREATE TABLE " + editorModPathsEntry.TABLE_MOD_PATH + " AS SELECT * FROM " + modPathsEntry.TABLE_MOD_PATH + ";");
        db.execSQL("CREATE TABLE " + editorModPrereqEntry.TABLE_REQUIREMENTS + " AS SELECT * FROM " + modPrereqEntry.TABLE_REQUIREMENTS + ";");
        db.close();
    }

    //Get modules for editor
    public ArrayList<Module> GetEditorModules(){
        ArrayList<Module> moduleList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "Select * FROM " +
                editorModulesEntry.TABLE_MODULES + ";";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String modId = cursor.getString(cursor.getColumnIndex(editorModulesEntry.COLUMN_MOD_ID));
                    String modName  = cursor.getString(cursor.getColumnIndex(editorModulesEntry.COLUMN_MOD_NAME));
                    String modDesc = cursor.getString(cursor.getColumnIndex(editorModulesEntry.COLUMN_MOD_DESC));
                    Integer modCredits = cursor.getInt(cursor.getColumnIndex(editorModulesEntry.COLUMN_MOD_CRED));
                    String modStatus = "active";
                    Integer modSemester = cursor.getInt(cursor.getColumnIndex(editorModulesEntry.COLUMN_MP_SEMESTER));
                    ArrayList<String> modPrereq = EditorGetModPrereq(modId);
                    ArrayList<String> modPath = EditorGetModPaths(modId);

                    moduleList.add(new Module(modId, modName, modDesc, modPrereq, modPath, modSemester, modCredits, modStatus));
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return moduleList;
    }
    public ArrayList<String> EditorGetModPaths(String modID){
        ArrayList<String> modPaths = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = " SELECT * FROM "
                + editorPathwaysEntry.TABLE_PATHWAYS + " JOIN " + editorModPathsEntry.TABLE_MOD_PATH + " ON " +
                editorPathwaysEntry.COLUMN_PATH_ID + " = " + editorModPathsEntry.COLUMN_MP_PATH_ID +
                " WHERE " + editorModPathsEntry.COLUMN_MP_MOD_ID + " = '" + modID + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String paths = cursor.getString(cursor.getColumnIndex(editorPathwaysEntry.COLUMN_PATH_NAME));
                    modPaths.add(paths);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return modPaths;
    }

    public ArrayList<String> EditorGetModPrereq(String modID){
        ArrayList<String> modPrereq = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = " SELECT "+ editorModPrereqEntry.COLUMN_REQ_REQMOD_ID +" FROM " + editorModPrereqEntry.TABLE_REQUIREMENTS +
                " WHERE " + editorModPrereqEntry.COLUMN_REQ_MOD_ID + " = '"  + modID + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String requiredMod = cursor.getString(cursor.getColumnIndex(editorModPrereqEntry.COLUMN_REQ_REQMOD_ID));
                    modPrereq.add(requiredMod);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return modPrereq;
    }

    // --------------------------------- END OF EDITOR STUFF --------------------------------- //

    // --------------------------------- PLANNER STUFF --------------------------------- //

    // Updated students module status when status is changed
    public void updateModuleStatus(Integer studId,String modID, String status){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(studentModuleEntry.COLUMN_SM_STATUS,status);
        db.update(studentModuleEntry.TABLE_STUD_MOD, values, studentModuleEntry.COLUMN_SM_STUD_ID + "= '" +studId.toString() +
                "' and " +studentModuleEntry.COLUMN_SM_MOD_ID +" = '"+ modID +"'", null);
        db.close();
    }

    // Add student to database
    public void AddStudent(int studID, String fName, String lName, String email){
        ContentValues values = new ContentValues();
        values.put(studentEntry.COLUMN_STUD_ID, studID);
        values.put(studentEntry.COLUMN_STUD_FNAME, fName);
        values.put(studentEntry.COLUMN_STUD_LNAME, lName);
        values.put(studentEntry.COLUMN_STUD_EMAIL, email);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(studentEntry.TABLE_STUDENTS, null, values);
        //Add student_mod here
        Cursor cursor = db.rawQuery("Select * from " + modulesEntry.TABLE_MODULES, null);
        List<String> fileName = new ArrayList<>();
        String status;
        if (cursor.moveToFirst()){
            Cursor cursor2 = db.rawQuery("Select * from " + modPrereqEntry.TABLE_REQUIREMENTS + " where " + modPrereqEntry.COLUMN_REQ_MOD_ID +
                    " = '" + cursor.getString(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_ID)) + "';", null);
            if(cursor2.moveToFirst()){
                status = "rnm";
            } else {
                status = "active";
            }
            db.execSQL("INSERT INTO " + studentModuleEntry.TABLE_STUD_MOD +
                    "("+ studentModuleEntry.COLUMN_SM_STUD_ID + "," + studentModuleEntry.COLUMN_SM_MOD_ID + ","+ studentModuleEntry.COLUMN_SM_STATUS +
                    ") VALUES (" + studID +",'"+cursor.getString(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_ID))+ "','"+ status+"');"
            );
            //fileName.add(cursor.getString(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_ID)));
            while (cursor.moveToNext()){
                cursor2 = db.rawQuery("Select * from " + modPrereqEntry.TABLE_REQUIREMENTS + " where " + modPrereqEntry.COLUMN_REQ_MOD_ID +
                        " = '" + cursor.getString(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_ID)) + "';", null);
                if(cursor2.moveToFirst()){
                    status = "rnm";
                } else {
                    status = "active";
                }
                db.execSQL("INSERT INTO " + studentModuleEntry.TABLE_STUD_MOD +
                        "("+ studentModuleEntry.COLUMN_SM_STUD_ID + "," + studentModuleEntry.COLUMN_SM_MOD_ID + ","+ studentModuleEntry.COLUMN_SM_STATUS +
                        ") VALUES (" + studID +",'"+cursor.getString(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_ID))+ "','"+ status+"');"
                );
                //fileName.add(cursor.getString(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_ID)));
            }
            cursor.close();
        }
        db.close();
    }

    //Remove student
    public void RemoveStudent(int studID){
        //Remove from mod_student
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + studentModuleEntry.TABLE_STUD_MOD +
                " WHERE " + studentModuleEntry.COLUMN_SM_STUD_ID + " = " + studID);
        db.execSQL("DELETE FROM " + studentEntry.TABLE_STUDENTS +
                " WHERE " + studentEntry.COLUMN_STUD_ID + " = " + studID);

        db.close();
    }

    //Used to check if student exists. For both adding and delete students.
    public boolean hasObject(String tableName, String dbfield, String fieldValue){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * from " + tableName + " where " + dbfield + " = '" + fieldValue +"'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount()<=0){
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    // Gets list of students for listview
    public ArrayList<Student> getListOfStudents(Integer studID){
        ArrayList<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query;

        if(studID==null){
            query = "SELECT * from " + studentEntry.TABLE_STUDENTS +";";
        } else {
            query = "SELECT * from " + studentEntry.TABLE_STUDENTS + " where " + studentEntry.COLUMN_STUD_ID + " LIKE '%" + studID + "%';";
        }
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Integer studId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(studentEntry.COLUMN_STUD_ID)));
                    String fname = cursor.getString(cursor.getColumnIndex(studentEntry.COLUMN_STUD_FNAME));
                    String lname = cursor.getString(cursor.getColumnIndex(studentEntry.COLUMN_STUD_LNAME));
                    String email = cursor.getString(cursor.getColumnIndex(studentEntry.COLUMN_STUD_EMAIL));
                    studentList.add(new Student(studId,fname,lname,email));
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return(studentList);
    }

    //Get students modules pathways
    //insert into pathways arraylist
    public ArrayList<Module> GetStudentModules(Integer studID){
        ArrayList<Module> moduleList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "Select * FROM " +
                studentModuleEntry.TABLE_STUD_MOD + " JOIN " + modulesEntry.TABLE_MODULES + " ON " +
                        studentModuleEntry.COLUMN_SM_MOD_ID + " = " + modulesEntry.COLUMN_MOD_ID +
                " WHERE " + studentModuleEntry.COLUMN_SM_STUD_ID + " = '" + studID + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String modId = cursor.getString(cursor.getColumnIndex(studentModuleEntry.COLUMN_SM_MOD_ID));
                    String modName  = cursor.getString(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_NAME));
                    String modDesc = cursor.getString(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_DESC));
                    Integer modCredits = cursor.getInt(cursor.getColumnIndex(modulesEntry.COLUMN_MOD_CRED));
                    String modStatus = cursor.getString(cursor.getColumnIndex(studentModuleEntry.COLUMN_SM_STATUS));
                    Integer modSemester = cursor.getInt(cursor.getColumnIndex(modulesEntry.COLUMN_MP_SEMESTER));
                    ArrayList<String> modPrereq = GetModPrereq(modId);
                    ArrayList<String> modPath = GetModPaths(modId);

                    moduleList.add(new Module(modId, modName, modDesc, modPrereq, modPath, modSemester, modCredits, modStatus));
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return moduleList;
    }

    public ArrayList<String> GetModPaths(String modID){
        ArrayList<String> modPaths = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = " SELECT * FROM "
                + pathwaysEntry.TABLE_PATHWAYS + " JOIN " + modPathsEntry.TABLE_MOD_PATH + " ON " +
                pathwaysEntry.COLUMN_PATH_ID + " = " + modPathsEntry.COLUMN_MP_PATH_ID +
                " WHERE " + modPathsEntry.COLUMN_MP_MOD_ID + " = '" + modID + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String paths = cursor.getString(cursor.getColumnIndex(pathwaysEntry.COLUMN_PATH_NAME));
                    modPaths.add(paths);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return modPaths;
    }

    public ArrayList<String> GetModPrereq(String modID){
        ArrayList<String> modPrereq = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = " SELECT "+ modPrereqEntry.COLUMN_REQ_REQMOD_ID +" FROM " + modPrereqEntry.TABLE_REQUIREMENTS +
                " WHERE " + modPrereqEntry.COLUMN_REQ_MOD_ID + " = '"  + modID + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String requiredMod = cursor.getString(cursor.getColumnIndex(modPrereqEntry.COLUMN_REQ_REQMOD_ID));
                    modPrereq.add(requiredMod);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return modPrereq;
    }

    public ArrayList<String> GetPathways(){
        ArrayList<String> pathwayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT * FROM " + pathwaysEntry.TABLE_PATHWAYS +
                " WHERE " + pathwaysEntry.COLUMN_PATH_NAME + " != " + "'Core';";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String pathwayString = cursor.getString(cursor.getColumnIndex(pathwaysEntry.COLUMN_PATH_NAME));
                    pathwayList.add(pathwayString);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return pathwayList;
    }

    public ArrayList<Integer> GetSemesters(){
        ArrayList<Integer> semesterList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = " SELECT DISTINCT " + modulesEntry.COLUMN_MP_SEMESTER + " FROM " + modulesEntry.TABLE_MODULES + ";";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do {
                    Integer semesterInt = cursor.getInt(cursor.getColumnIndex(modulesEntry.COLUMN_MP_SEMESTER));
                    semesterList.add(semesterInt);
                } while(cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return semesterList;
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
        if(prereq!=null) {
            for (String s : prereq) {
                db.execSQL(
                        "INSERT INTO "+modPrereqEntry.TABLE_REQUIREMENTS+
                                "("+modPrereqEntry.COLUMN_REQ_MOD_ID+","+modPrereqEntry.COLUMN_REQ_REQMOD_ID+")" +
                                " VALUES ('"+modID+"','"+s+"');"
                );
            }
        }

        if(pathways.size()>0){
            for(Integer pathID : pathways){
                db.execSQL(
                        "INSERT INTO "+modPathsEntry.TABLE_MOD_PATH+
                                "("+modPathsEntry.COLUMN_MP_MOD_ID+","+modPathsEntry.COLUMN_MP_PATH_ID+")" +
                                " VALUES ('"+modID+"','"+pathID+"');"
                );
            }
        } else {
            db.execSQL(
                    "INSERT INTO "+modPathsEntry.TABLE_MOD_PATH+
                            "("+modPathsEntry.COLUMN_MP_MOD_ID+","+modPathsEntry.COLUMN_MP_PATH_ID+")" +
                            " VALUES ('"+modID+"',5);"
            );
        }
    }

    public void AddPathways(SQLiteDatabase db, String pathwayName){
        //Add module to module table
        ContentValues values = new ContentValues();
        values.put(pathwaysEntry.COLUMN_PATH_NAME, pathwayName);
        db.insert(pathwaysEntry.TABLE_PATHWAYS, null, values);
    }

    // --------------------------------- INSERT STATEMENTS --------------------------------- //

    //populate
    public void PopulateDB(){
        SQLiteDatabase db = getWritableDatabase();
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
        //Database, ID, Name, credits, semester, description, ArrayList<String> Pre-reqs, ArrayList<Integer> pathways
        //this.AddModule(db, "", "",null,15, 2,"Info501 desc", null);

        // Leaving prequeList empty(null), results in no requirements for the paper
        // Leaving pathwayList empty(null), results in the module being defined as Core pathway.
        ArrayList<String> prequeList = new ArrayList<>();
        ArrayList<Integer> pathwayList = new ArrayList<>();
        //prequeList.add("");
        //pathwayList.add("");

        // ------------------------------------------- Semester 1 ------------------------------------------- //
        // ----- MODULE 1 ----- //
        this.AddModule(db, "COMP501", "IT Operations",15, 1,"COMP501 desc",prequeList, pathwayList);
        // ----- MODULE 2 ----- //
        this.AddModule(db, "COMP502", "Fundamentals of Programming and Problem Solving",15, 1,"COMP502 desc", prequeList, pathwayList);
        // ----- MODULE 3 ----- //
        this.AddModule(db, "INFO501", "Professional Practice",15, 1,"INFO501 desc", prequeList, pathwayList);
        // ----- MODULE 4 ----- //
        this.AddModule(db, "INFO502", "Business Systems Analysis & Design",15, 1,"INFO502 desc", prequeList, pathwayList);

        // ------------------------------------------- Semester 2 ------------------------------------------- //
        // ----- MODULE 1 ----- //
        this.AddModule(db, "COMP503", "Introduction to Networks (Cisco 1)",15, 2,"COMP503 desc", prequeList, pathwayList);
        // ----- MODULE 2 ----- //
        this.AddModule(db, "COMP504", "Operating Systems & Systems Support",15, 2,"COMP504 desc", prequeList, pathwayList);
        // ----- MODULE 3 ----- //
        this.AddModule(db, "INFO503", "Database Principles",15, 2,"INFO503 desc", prequeList, pathwayList);
        // ----- MODULE 4 ----- //
        this.AddModule(db, "INFO504", "Technical Support",15, 2,"INFO504 desc", prequeList, pathwayList);

        // ------------------------------------------- Semester 3 ------------------------------------------- //
        // ----- MODULE 1 ----- //
        prequeList = new ArrayList<>();
        prequeList.add("COMP502");
        this.AddModule(db, "COMP601", "Object Oriented Programming",15, 3,"COMP601 desc",prequeList, pathwayList);
        // ----- MODULE 2 ----- //
        prequeList = new ArrayList<>();
        prequeList.add("INFO503");
        this.AddModule(db, "INFO601", "Data-modelling and SQL",15, 3,"INFO601 desc",prequeList, pathwayList);
        // ----- MODULE 3 ----- //
        prequeList = new ArrayList<>();
        this.AddModule(db, "MATH601", "Mathematics for IT",15, 3,"MATH601 desc",prequeList, pathwayList);
        // ----- MODULE 4 ----- //
        prequeList = new ArrayList<>();
        prequeList.add("COMP502");
        prequeList.add("INFO502");
        this.AddModule(db, "COMP602", "Web Development",15, 3,"COMP602 desc",prequeList, pathwayList);

        // ------------------------------------------- Semester 4 ------------------------------------------- //
        // ----- MODULE 1 ----- //
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO602", "Business, Interpersonal Communications & Technical Writing",15, 4,"INFO602 desc",prequeList, pathwayList);

        // ----- MODULE 2 ----- //
        // Software & Database
        pathwayList = new ArrayList<>();
        pathwayList.add(1);
        pathwayList.add(3);
        prequeList = new ArrayList<>();
        prequeList.add("COMP601");
        prequeList.add("MATH601");
        this.AddModule(db, "COMP605", "Data Structures and Algorithms",15, 4,"COMP605 desc",prequeList, pathwayList);

        // Networking
        pathwayList = new ArrayList<>();
        pathwayList.add(2);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP615", "Data Centre Infrastructure",15, 4,"COMP615 desc",prequeList, pathwayList);

        // Multimedia
        pathwayList = new ArrayList<>();
        pathwayList.add(4);
        prequeList = new ArrayList<>();
        prequeList.add("COMP602");
        this.AddModule(db, "COMP603", "The Web Environment",15, 4,"COMP603 desc",prequeList, pathwayList);

        // ----- MODULE 3 ----- //

        // Software
        pathwayList = new ArrayList<>();
        pathwayList.add(1);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP609", "Applications Development",15, 4,"COMP609 desc",prequeList, pathwayList);

        // Networking
        pathwayList = new ArrayList<>();
        pathwayList.add(2);
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO603", "Systems Administration",15, 4,"INFO603 desc",prequeList, pathwayList);

        // Database & Multi media
        pathwayList = new ArrayList<>();
        pathwayList.add(3);
        pathwayList.add(4);
        prequeList = new ArrayList<>();
        prequeList.add("COMP602");
        this.AddModule(db, "COMP606", "Web Programming",15, 4,"COMP606 desc",prequeList, pathwayList);

        // ----- MODULE 4 ----- //

        // Software
        pathwayList = new ArrayList<>();
        pathwayList.add(1);
        prequeList = new ArrayList<>();
        prequeList.add("MATH601");
        this.AddModule(db, "MATH602", "Mathematics for Programming",15, 4,"MATH602 desc",prequeList, pathwayList);

        // Networking
        pathwayList = new ArrayList<>();
        pathwayList.add(2);
        prequeList = new ArrayList<>();
        prequeList.add("COMP503");
        this.AddModule(db, "COMP604", "Routing and Switching Essentials",15, 4,"COMP604 desc",prequeList, pathwayList);

        // Database
        pathwayList = new ArrayList<>();
        pathwayList.add(3);
        prequeList = new ArrayList<>();
        prequeList.add("INFO503");
        this.AddModule(db, "INFO604", "Database Systems",15, 4,"INFO604 desc",prequeList, pathwayList);

        // Multimedia
        pathwayList = new ArrayList<>();
        pathwayList.add(4);
        prequeList = new ArrayList<>();
        prequeList.add("COMP602");
        this.AddModule(db, "COMP607", "Visual Effects and Animation",15, 4,"COMP607 desc",prequeList, pathwayList);

        // ------------------------------------------- Semester 5 ------------------------------------------- //

        // ----- MODULE 1 ----- //

        // Software & Database
        pathwayList = new ArrayList<>();
        pathwayList.add(1);
        pathwayList.add(3);
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO704", "Data-Warehousing and Business Intelligence",15, 5,"INFO704 desc",prequeList, pathwayList);

        // Networking
        pathwayList = new ArrayList<>();
        pathwayList.add(2);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP701", "Advanced Networking",15, 5,"COMP701 desc",prequeList, pathwayList);

        // Multimedia
        pathwayList = new ArrayList<>();
        pathwayList.add(4);
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO709", "Human Computer Interaction",15, 5,"INFO709 desc",prequeList, pathwayList);

        // ----- MODULE 2 ----- //

        // Software
        pathwayList = new ArrayList<>();
        pathwayList.add(1);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP707", "Principles of Software Testing",15, 5,"COMP707 desc",prequeList, pathwayList);

        // Networking & Multimedia
        pathwayList = new ArrayList<>();
        pathwayList.add(2);
        pathwayList.add(4);
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO702", "Cyber-Security",15, 5,"INFO702 desc",prequeList, pathwayList);

        // Database
        pathwayList = new ArrayList<>();
        pathwayList.add(3);
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO706", "Database Front-End Applications",15, 5,"INFO706 desc",prequeList, pathwayList);

        // ----- MODULE 3 ----- //

        // Software & Database & Multimedia
        pathwayList = new ArrayList<>();
        pathwayList.add(1);
        pathwayList.add(3);
        pathwayList.add(4);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP709", "Mobile Applications Development",15, 5,"COMP709 desc",prequeList, pathwayList);

        // Networking
        pathwayList = new ArrayList<>();
        pathwayList.add(2);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP704", "Network Security",15, 5,"COMP704 desc",prequeList, pathwayList);

        // ----- MODULE 4 ----- //

        // Software
        pathwayList = new ArrayList<>();
        pathwayList.add(1);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP706", "Game Development",15, 5,"COMP706 desc",prequeList, pathwayList);

        // Networking
        pathwayList = new ArrayList<>();
        pathwayList.add(2);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP702", "Scaling Networks",15, 5,"COMP702 desc",prequeList, pathwayList);

        // Database
        pathwayList = new ArrayList<>();
        pathwayList.add(3);
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO707", "Cloud Server Databases",15, 5,"INFO707 desc",prequeList, pathwayList);

        // Multimedia
        pathwayList = new ArrayList<>();
        pathwayList.add(4);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP710", "Web Applications Development",15, 5,"COMP710 desc",prequeList, pathwayList);



        // ------------------------------------------- Semester 6 ------------------------------------------- //

        // ----- MODULE 1 ----- //

        // Core
        pathwayList = new ArrayList<>();
        prequeList = new ArrayList<>();
        prequeList.add("INFO602");
        this.AddModule(db, "BIZM701", "Business Essentials for IT Professionals",15, 6,"BIZM701 desc",prequeList, pathwayList);

        // ----- MODULE 2 ----- //

        // Software & Database
        pathwayList = new ArrayList<>();
        pathwayList.add(1);
        pathwayList.add(3);
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO703", "Big Data and Analytics",15, 6,"INFO703 desc",prequeList, pathwayList);

        // Networking
        pathwayList = new ArrayList<>();
        pathwayList.add(2);
        prequeList = new ArrayList<>();
        this.AddModule(db, "COMP705", "Connecting Networks",15, 6,"COMP705 desc",prequeList, pathwayList);

        // Multimedia
        pathwayList = new ArrayList<>();
        pathwayList.add(4);
        prequeList = new ArrayList<>();
        this.AddModule(db, "INFO708", "Data Visualisation",15, 6,"INFO708 desc",prequeList, pathwayList);
        // ----- MODULE 3 ----- //
        pathwayList = new ArrayList<>();
        prequeList = new ArrayList<>();
        this.AddModule(db, "PROJ", "Capstone Project/Internship/Design Factory",30, 6,"PROJ desc",prequeList, pathwayList);
        // ----- MODULE 4 ----- //
        // None: due to 30 credit paper

        db.close();
    }
}

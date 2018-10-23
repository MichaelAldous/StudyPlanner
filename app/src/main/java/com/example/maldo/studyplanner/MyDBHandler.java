package com.example.maldo.studyplanner;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.maldo.studyplanner.Data.*;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "planner.db";

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PATHWAY_TABLE = "CREATE TABLE " + pathwayEntry.TABLE_PATHWAYS + "( " +
                pathwayEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                pathwayEntry.COLUMN_PATH_NAME + " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_PATHWAY_TABLE);
        final String SQL_CREATE_MODULES_TABLE = "CREATE TABLE " + modpathEntry.COLUMN_MP_PATH_ID + "( " +
                modpathEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                modpathEntry.TABLE_MOD_PATH + " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_MODULES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    // PATHWAYS TABLE
//    public static final String TABLE_PATHWAYS = "pathways";
//    public static final String COLUMN_PATH_ID = "_path_id"; // auto increment number?
//    public static final String COLUMN_PATH_NAME = "_path_name"; // "Core", "Software Engineering", "Database Architecture", "Networking", "Multi Media Web Development"
//
//    // MODULES TABLE
//    public static final String TABLE_MODULES = "modules";
//    public static final String COLUMN_MOD_ID = "_mod_id"; //"INFO703" etc.
//    public static final String COLUMN_MOD_NAME = "_mod_name"; //Mobile App Dev
//    public static final String COLUMN_MOD_PREREQ = "_mod_prereq"; // "MAT501", can be null etc.
//    public static final String COLUMN_MP_SEMESTER = "_mp_semester"; // FK
//    public static final String COLUMN_MOD_DESC = "_mod_desc"; // "For mobile app we make apps blah blah blah"
//
//    // MOD PATH TABLE
//    public static final String TABLE_MOD_PATH = "mod_path";
//    public static final String COLUMN_MP_MOD_ID = "_mp_mod_id"; // PK FK
//    public static final String COLUMN_MP_PATH_ID = "_mp_path_id"; // PK FK
//
//    // STUDENT TABLE
//    public static final String TABLE_STUDENTS = "student";
//    public static final String COLUMN_STUD_ID = "_stud_id"; // PK 17451234
//    public static final String COLUMN_STUD_FNAME = "_stud_fname"; // "Bob"
//    public static final String COLUMN_STUD_LNAME = "_stud_lname"; // "Johns"
//    public static final String COLUMN_STUD_EMAIL = "_stud_email"; // "bobjohns@student.wintec.ac.nz"
//
//    //STUDENT MODULE TABLE
//    public static final String TABLE_STUD_MOD = "stud_mod";
//    public static final String COLUMN_SM_STUD_ID = "_sm_stud_id"; // PK FK
//    public static final String COLUMN__SM_MOD_ID = "_sm_mod_id"; // PK FK
//    public static final String COLUMN__SM_STATUS = "_sm_status"; //"active", "inactive", "passed"

}

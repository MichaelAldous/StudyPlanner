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

    public void onCreate(SQLiteDatabase db)
    {
        // creating the pathways table
        final String SQL_CREATE_PATHWAYS_TABLE = "CREATE TABLE " + pathwaysEntry.TABLE_PATHWAYS + "( " +
                pathwaysEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                pathwaysEntry.COLUMN_PATH_NAME+ " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_PATHWAYS_TABLE);

        // creating the modules table
        final String SQL_CREATE_MODULES_TABLE = "CREATE TABLE " + modulesEntry.TABLE_MODULES + "( " +
                modulesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                modulesEntry.COLUMN_MOD_NAME+ " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MOD_PREREQ + " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MP_SEMESTER + " TEXT NOT NULL, " +
                modulesEntry.COLUMN_MOD_DESC + "TEXT NOT NULL "+ ");";
        db.execSQL(SQL_CREATE_MODULES_TABLE);

        // creating the mod path table
        final String SQL_CREATE_MODPATH_TABLE = "CREATE TABLE " + modPathsEntry.TABLE_MOD_PATH + "( " +
                modPathsEntry.COLUMN_MP_MOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                modPathsEntry.COLUMN_MP_PATH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT "+ ");";
        db.execSQL(SQL_CREATE_MODPATH_TABLE);

        // creating the student table
        final String SQL_CREATE_STUDENT_TABLE = "CREATE TABLE " + studentEntry.TABLE_STUDENTS + "( " +
                studentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                studentEntry.COLUMN_STUD_FNAME+ " TEXT NOT NULL, " +
                studentEntry.COLUMN_STUD_LNAME+ " TEXT NOT NULL, " +
                studentEntry.COLUMN_STUD_EMAIL+ " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_STUDENT_TABLE);

        // creating the student module table
        final String SQL_CREATE_STUDENTMODULE_TABLE = "CREATE TABLE " + studentModuleEntry.TABLE_STUD_MOD + "( " +
                studentModuleEntry.COLUMN_SM_STUD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                studentModuleEntry.COLUMN__SM_MOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                studentModuleEntry.COLUMN__SM_STATUS + " TEXT NOT NULL "  + ");";
        db.execSQL(SQL_CREATE_STUDENTMODULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // for updating the database
        db.execSQL("DROP TABLE IF EXISTS " + pathwaysEntry.TABLE_PATHWAYS);
        db.execSQL("DROP TABLE IF EXISTS " + modulesEntry.TABLE_MODULES);
        db.execSQL("DROP TABLE IF EXISTS " + modPathsEntry.TABLE_MOD_PATH);
        db.execSQL("DROP TABLE IF EXISTS " + studentEntry.TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + studentModuleEntry.TABLE_STUD_MOD);
        onCreate(db);
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

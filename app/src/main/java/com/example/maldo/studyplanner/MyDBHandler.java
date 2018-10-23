package com.example.maldo.studyplanner;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "planner.db";

    // PATHWAYS TABLE
    public static final String TABLE_PATHWAYS = "pathways";
    public static final String COLUMN_PATH_ID = "_path_id"; // auto increment number?
    public static final String COLUMN_PATH_NAME = "_path_name"; // "Core", "Software Engineering", "Database Architecture", "Networking", "Multi Media Web Development"

    // MODULES TABLE
    public static final String TABLE_MODULES = "modules";
    public static final String COLUMN_MOD_ID = "_mod_id"; //"INFO703" etc.
    public static final String COLUMN_MOD_NAME = "_mod_name"; //Mobile App Dev
    public static final String COLUMN_MOD_PREREQ = "_mod_prereq"; // "MAT501" etc.
    public static final String COLUMN_MOD_SEMESTER = "_mp_semester"; // FK
    public static final String COLUMN_MOD_DESC = "_mod_desc"; // "For mobile app we make apps blah blah blah"

    // MOD PATH TABLE
    public static final String TABLE_MOD_PATH = "mod_path";
    public static final String COLUMN_MP_MOD_ID = "_mp_mod_id"; // PK FK
    public static final String COLUMN_MP_PATH_ID = "_mp_path_id"; // PK FK

    // STUDENT TABLE
    public static final String TABLE_STUDENTS = "student";
    public static final String COLUMN_STUD_ID = "_stud_id"; // PK 17451234
    public static final String COLUMN_STUD_FNAME = "_stud_fname"; // "Bob"
    public static final String COLUMN_STUD_LNAME = "_stud_lname"; // "Johns"
    public static final String COLUMN_STUD_EMAIL = "_stud_email"; // "bobjohns@student.wintec.ac.nz"
    public static final String COLUMN_STUD_PATH = "_stud_path"; // references pathways table

    //STUDENT MODULE TABLE
    public static final String TABLE_STUD_MOD = "stud_mod";
    public static final String COLUMN_SM_STUD_ID = "_sm_stud_id"; // PK FK
    public static final String COLUMN_SM_MOD_ID = "_sm_mod_id"; // PK FK
    public static final String COLUMN_SM_STATUS = "_sm_status"; //"active", "inactive", "passed"


    @Override
    public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE pathways
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_PATHWAYS + " ( " +
                COLUMN_PATH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PATH_NAME + " TEXT " +
                ");");

        // CREATE TABLE modules
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MODULES + " ( " +
                COLUMN_MOD_ID + "TEXT PRIMARY KEY, " +
                COLUMN_MOD_NAME + " TEXT, " +
                COLUMN_MOD_SEMESTER + " TEXT, " +
                COLUMN_MOD_PREREQ + " TEXT, " +
                COLUMN_MOD_DESC + " TEXT " +
                ");");

        // CREATE TABLE mod_path
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MOD_PATH + " ( " +
                COLUMN_MP_MOD_ID + " TEXT, " +
                COLUMN_MP_PATH_ID + " TEXT, " +
                "FOREIGN KEY (" + COLUMN_MP_MOD_ID + ") REFERENCES "+ TABLE_MODULES + "( " + COLUMN_MOD_ID + " ), " +
                "FOREIGN KEY (" + COLUMN_MP_PATH_ID + ") REFERENCES "+ TABLE_PATHWAYS + "(" + COLUMN_PATH_ID + "), " +
                "PRIMARY KEY(" + COLUMN_MP_MOD_ID +","+ COLUMN_MP_PATH_ID +") " +
                ");");

        // CREATE TABLE student
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_STUDENTS + " ( " +
                        COLUMN_STUD_ID + "INTEGER PRIMARY KEY, " +
                        COLUMN_STUD_FNAME + " TEXT, " +
                        COLUMN_STUD_LNAME + " TEXT, " +
                        COLUMN_STUD_EMAIL + " TEXT, " +
                        COLUMN_STUD_PATH + " INTEGER, " +
                        "FOREIGN KEY (" + COLUMN_STUD_PATH + ") REFERENCES " +
                        TABLE_PATHWAYS + "(" + COLUMN_PATH_ID + ")" +
                        "); ");

        // CREATE TABLE stud_mod
        db.execSQL("CREATE TABLE " + TABLE_STUD_MOD + "(" +
                        COLUMN_SM_STUD_ID + " INTEGER, " +
                        COLUMN_SM_MOD_ID + " TEXT, " +
                        "FOREIGN KEY (" + COLUMN_SM_STUD_ID + ") REFERENCES " +  TABLE_STUDENTS + "(" + COLUMN_STUD_ID + ")," +
                        "FOREIGN KEY (" + COLUMN_SM_MOD_ID + ") REFERENCES " +  TABLE_MODULES + "(" + COLUMN_MOD_ID + ")," +
                        "PRIMARY KEY (" + COLUMN_SM_STUD_ID +","+ COLUMN_SM_MOD_ID +") " +
                        "); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUD_MOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOD_PATH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATHWAYS);
        onCreate(db);
    }

    //Add a new student to the database
    public void addStudent(int studentID, String fname, String lname, String email, int pathway) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUD_ID, studentID);
        values.put(COLUMN_STUD_FNAME, fname);
        values.put(COLUMN_STUD_LNAME, lname);
        values.put(COLUMN_STUD_EMAIL, email);
        values.put(COLUMN_STUD_PATH, pathway);
        //Database we are going to write to
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_STUDENTS, null, values);
        //Close database to free up memory
        db.close();
    }


    //Delete student from the database
    public void deleteStudent(int studentID) {
        //Refrence to database
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_STUD_MOD + " WHERE " + COLUMN_SM_STUD_ID + " = '" + studentID + "';");
        db.execSQL("DELETE FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUD_ID + " = '" + studentID + "';");
    }
}

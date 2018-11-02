package com.example.maldo.studyplanner;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

public class ActivitySplash extends AppCompatActivity {
    Handler handler;
    MyDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbHandler = new MyDBHandler(this);

        if(!checkDB(this, "planner.db")){
            //Create database
            Log.d("checkDB", "onCreate: DB DOES NOT EXIST");
        } else {
            Log.d("checkDB", "onCreate: DB DOES EXIST");
            //Check DB versions
        }

        //Splash screen
        //Waits 3 seconds until loading the main activity
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(ActivitySplash.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }

    private static boolean checkDB(Context context, String dbName){
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}

package com.example.maldo.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class ActivitySplash extends AppCompatActivity {
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
        },1000);

    }


}

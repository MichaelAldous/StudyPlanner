package com.example.maldo.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ActivityAdmin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        Button loginButton = findViewById(R.id.buttonLogin);

        //onClick listener for cake button, passes cake recipes to new intent
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAdmin.this, ActivityEditor.class);
                startActivity(intent);
            }
        });
    }
}

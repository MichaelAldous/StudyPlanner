package com.example.maldo.studyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityAdmin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final String password = "WinITDMP01";

        final EditText pwInput = (EditText) findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.buttonLogin);

        //onClick listener for cake button, passes cake recipes to new intent
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pwInput.getText().toString().equals(password)) {
                    Intent intent = new Intent(ActivityAdmin.this, ActivityEditor.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_LONG).show();
            }
         }
        });
    }
}

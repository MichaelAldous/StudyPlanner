package com.example.maldo.studyplanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Matcher;

import static android.support.v7.app.AlertDialog.*;

public class ActivityManageStudents extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        Button addButton = findViewById(R.id.buttonAddStudent);
        Button deleteButton = findViewById(R.id.buttonDeleteStudent);

        final EditText studIdTV = findViewById(R.id.studentIDEditText);
        final EditText fNameTV = findViewById(R.id.studentFNameEditText);
        final EditText lNameTV = findViewById(R.id.studentLNameEditText);
        final EditText emailTV = findViewById(R.id.studentEmailEditText);

        // add Student to Database
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studIDString = studIdTV.getText().toString();
                String fName = fNameTV.getText().toString();
                String lName = lNameTV.getText().toString();
                String email = emailTV.getText().toString();

                if(studIDString!=null){
                    try{
                        // If studID is number, else throws error
                        Integer studID = Integer.parseInt(studIDString);
                        if(TextUtils.isEmpty(fName)==false){
                            if(TextUtils.isEmpty(lName)==false){
                                if(email!=null){
                                    //Check if valid email
                                    Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
                                    if(matcher.matches()){
                                        // Add student to DB
                                        // TODO: Check if student exists
                                        MyDBHandler handler = new MyDBHandler(getApplicationContext());
                                        if(handler.hasObject(Data.studentEntry.TABLE_STUDENTS,Data.studentEntry.COLUMN_STUD_ID,studIDString)){
                                            Toast.makeText(getApplicationContext(), "Student Already Exists",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            int studentIDPass = Integer.parseInt(studIDString);
                                            handler.AddStudent(studentIDPass,fName,lName,email);
                                            Toast.makeText(getApplicationContext(), "Student Added",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        // Please enter a valid email
                                        Toast.makeText(getApplicationContext(), "Please enter a valid email",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    // Please enter email
                                    Toast.makeText(getApplicationContext(), "Please enter an email",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // Please enter last name
                                Toast.makeText(getApplicationContext(), "Please enter a last name",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // Please enter First name
                            Toast.makeText(getApplicationContext(), "Please enter a first name",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch(NumberFormatException e) {
                        // If studID is not a number
                        // Please enter stud ID as number
                        Toast.makeText(getApplicationContext(), "Please enter a valid student ID (numbers only)",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Please enter student ID
                    Toast.makeText(getApplicationContext(), "Please enter a student ID",
                            Toast.LENGTH_LONG).show();
                }
            }
        }); //End of addButton.setOnClick

        // Remove student from DB
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studIDString = studIdTV.getText().toString();
                if(studIDString!=null){
                    try{
                        // If studID is number
                        final Integer studID = Integer.parseInt(studIDString);
                        // TODO - SEARCH FOR STUDENT IN DB
                        final MyDBHandler handler = new MyDBHandler(getApplicationContext());

                        if(handler.hasObject(Data.studentEntry.TABLE_STUDENTS,Data.studentEntry.COLUMN_STUD_ID,studIDString)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityManageStudents.this);
                            builder.setCancelable(true);
                            builder.setTitle("Remove student?");
                            builder.setMessage("Are you sure you want to remove student: " + studID + "?");
                            builder.setPositiveButton("Confirm",
                                    new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which){
                                            handler.RemoveStudent(studID);
                                            Toast.makeText(getApplicationContext(), "Student Deleted",
                                                    Toast.LENGTH_LONG).show();
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
                            int studentIDPass = Integer.parseInt(studIDString);
                            Toast.makeText(getApplicationContext(), "Student does not exist",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch(NumberFormatException e) {
                        // If studID is not a number
                        // Please enter stud ID as number
                        Toast.makeText(getApplicationContext(), "Please enter a valid student ID (numbers only)",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Please enter student ID
                    Toast.makeText(getApplicationContext(), "Please enter a student ID",
                            Toast.LENGTH_LONG).show();
                }
            }
        }); //End of addButton.setOnClick


    }
}

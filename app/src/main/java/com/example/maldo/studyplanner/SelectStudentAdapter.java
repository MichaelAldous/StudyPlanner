package com.example.maldo.studyplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/*
    This is used for the select student activity
    Gets values from database, and sets values in list row
 */
public class SelectStudentAdapter extends ArrayAdapter<Student> {
    public SelectStudentAdapter(Context context, ArrayList<Student> students){
        super(context,0,students);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Student student = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.student_list_item,parent,false);
        }

        TextView tvFname = (TextView) convertView.findViewById(R.id.sli_fname);
        TextView tvLname = (TextView) convertView.findViewById(R.id.sli_lname);
        TextView tvId = (TextView) convertView.findViewById(R.id.sli_id);

        tvFname.setText(student.getStudentFName());
        tvLname.setText(student.getStudentLName());
        tvId.setText(String.valueOf(student.getStudentId()));

        return convertView;
    }

}

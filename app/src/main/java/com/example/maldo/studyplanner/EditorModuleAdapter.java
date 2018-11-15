package com.example.maldo.studyplanner;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class EditorModuleAdapter extends ArrayAdapter<Module> {
    public EditorModuleAdapter(Context context, ArrayList<Module> modules){
        super(context,0,modules);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Module module = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.editor_list_item,parent,false);
        }

        EditText etModID = (EditText) convertView.findViewById(R.id.eli_mod_id);
        EditText etModName = (EditText) convertView.findViewById(R.id.eli_mod_name);
        EditText etCredits = (EditText) convertView.findViewById(R.id.eli_credits);
        EditText etPrereq = (EditText) convertView.findViewById(R.id.eli_prereq);

        etModID.setText(module.getModuleId());
        etModName.setText(module.getModuleName());
        etCredits.setText(String.valueOf(module.getModuleCredits()));

        ArrayList<String> prereq = module.getModulePrereqs();
        String prereqString = "";
        for(String pr: prereq){
            prereqString = prereqString + ", " + pr;
        }
        if(prereqString != null && prereqString.length() > 0){
            prereqString = prereqString.substring(2, prereqString.length());
        } else if (prereqString == ""){
            prereqString = "None";
        }
        etPrereq.setText(prereqString);
        return convertView;
    }
}

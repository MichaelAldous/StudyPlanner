package com.example.maldo.studyplanner;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlannerModuleAdapter  extends ArrayAdapter<Module> {
    public PlannerModuleAdapter(Context context, ArrayList<Module> modules){
        super(context,0,modules);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Module module = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.planner_list_item,parent,false);
        }

        TextView tvModID = (TextView) convertView.findViewById(R.id.pli_modId);
        TextView tvModName = (TextView) convertView.findViewById(R.id.pli_modName);
        TextView tvCredits = (TextView) convertView.findViewById(R.id.pli_credits);
        TextView tvPrereq = (TextView) convertView.findViewById(R.id.pli_prereq);

        tvModID.setText(module.getModuleId());
        tvModName.setText(module.getModuleName());
        tvCredits.setText(String.valueOf(module.getModuleCredits()));

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
        tvPrereq.setText(prereqString);

        this.setColour(convertView, module);
        return convertView;
    }

    private void setColour(View view, Module module){
        Log.d("PARENT", "setColour: " + module.getModuleStatus());
        if(module.getModuleStatus().equals("rnm")){
            view.setBackgroundColor(Color.RED);
        } else if (module.getModuleStatus().equals("passed")){
            view.setBackgroundColor(Color.GREEN);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }

    }
}
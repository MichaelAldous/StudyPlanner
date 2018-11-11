package com.example.maldo.studyplanner;

import android.content.Context;
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

        tvModID.setText(module.getModuleId());
        tvModName.setText(module.getModuleName());
        tvCredits.setText(String.valueOf(module.getModuleCredits()));

        return convertView;
    }
}
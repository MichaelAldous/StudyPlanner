package com.example.maldo.studyplanner;

import java.util.ArrayList;

public class Module {
    private String id;
    private String name;
    private String description;
    private ArrayList<String> prereqs;
    private ArrayList<String> pathways;
    private Integer semester;
    private Integer credits;
    private String status;

    public Module(String id,String name, String description, ArrayList<String> prereqs, ArrayList<String> paths, Integer semester, Integer credits, String status){
        this.id = id;
        this.name = name;
        this.description = description;
        this.prereqs = prereqs;
        this.pathways = paths;
        this.semester = semester;
        this.credits = credits;
        this.status = status;
    }

    public String getModuleId(){
        return this.id;
    }
    public String getModuleName(){
        return this.name;
    }
    public String getModuleDescription(){
        return this.description;
    }
    public ArrayList<String> getModulePrereqs(){ return this.prereqs; }
    public Integer getModuleSemester(){
        return this.semester;
    }
    public Integer getModuleCredits() {
        return this.credits;
    }
    public String getModuleStatus(){
        return this.status;
    }
    public ArrayList<String> getPathways() {
        return pathways;
    }

    public void setModuleStatus(String status){
        this.status = status;
    }
    public void setModId(String id){
        this.id = id;
    }
    public void setModName(String name){
        this.name = name;
    }
    public void setModDesc(String desc){
        this.description = desc;
    }
    public void setModPrereqs(ArrayList<String> prereqs){
        this.prereqs = prereqs;
    }
    public void setModPathways(ArrayList<String> pathways){
        this.pathways = pathways;
    }
    public void setModSemester(Integer semester){
        this.semester = semester;
    }
    public void setModCredits(Integer credits){
        this.credits = credits;
    }

}

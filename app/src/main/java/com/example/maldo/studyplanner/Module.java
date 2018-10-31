package com.example.maldo.studyplanner;

public class Module {
    private String id;
    private String name;
    private String description;
    private String[] prereqs;
    private String semester;
    private Integer credits;
    private String status;

    public Module(String id,String name, String description, String[] prereqs, String semester, Integer credits, String status){
        this.id = id;
        this.name = name;
        this.description = description;
        this.prereqs = prereqs;
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
    public String[] getModulePrereqs(){
        return this.prereqs;
    }
    public String getModuleSemester(){
        return this.semester;
    }
    public Integer getModuleCredits() {
        return this.credits;
    }
    public String getModuleStatus(){
        return this.status;
    }

    public void setModuleStatus(String status){
        this.status = status;
    }
}

package com.example.maldo.studyplanner;

public class Student {
    private Integer student_id;
    private String student_fname;
    private String student_lname;
    private String student_email;

    public Student(Integer id, String fname, String lname, String email){
        this.student_id = id;
        this.student_fname = fname;
        this.student_lname = lname;
        this.student_email = email;
    }

    public String getStudentFName(){
        return this.student_fname;
    }

    public String getStudentLName(){
        return this.student_lname;
    }

    public Integer getStudentId(){ return this.student_id; }

    public String getStudentEmail(){
        return this.student_email;
    }

}

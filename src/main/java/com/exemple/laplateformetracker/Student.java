package com.exemple.laplateformetracker;

import java.util.ArrayList;

public class Student {
    private int id;
    private String fName;
    private String lName;
    private int age;
    private ArrayList<Integer> grades;
    
    public Student(int id, String fName, String lName, int age, ArrayList<Integer> grades) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.age = age;
        this.grades = grades;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return fName;
    }

    public String getLastName() {
        return lName;
    }

    public int getAge() {
        return age;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String fName) {
        this.fName = fName;
    }

    public void setLastName(String lName) {
        this.lName = lName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGrades(ArrayList<Integer> grades) {
        this.grades = grades;
    }
}

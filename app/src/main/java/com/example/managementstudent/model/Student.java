package com.example.managementstudent.model;

import java.util.Date;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private String className;
    private String birthDay;
    private String position;
    private String faculty;
    private String industry;
    private String imgStudent;
    private List<Certificate> certificateList;

    public Student(String id, String name, String className, String birthDay, String position, String faculty, String industry, String imgStudent, List<Certificate> certificateList) {
        this.id = id;
        this.name = name;
        this.className = className;
        this.birthDay = birthDay;
        this.position = position;
        this.faculty = faculty;
        this.industry = industry;
        this.imgStudent = imgStudent;
        this.certificateList = certificateList;
    }

    public Student(String name, String className, String birthDay, String position, String faculty, String industry, String imgStudent, List<Certificate> certificateList) {
        this.name = name;
        this.className = className;
        this.birthDay = birthDay;
        this.position = position;
        this.faculty = faculty;
        this.industry = industry;
        this.imgStudent = imgStudent;
        this.certificateList = certificateList;
    }

    public Student() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getImgStudent() {
        return imgStudent;
    }

    public void setImgStudent(String imgStudent) {
        this.imgStudent = imgStudent;
    }

    public List<Certificate> getCertificateList() {
        return certificateList;
    }

    public void setCertificateList(List<Certificate> certificateList) {
        this.certificateList = certificateList;
    }
}

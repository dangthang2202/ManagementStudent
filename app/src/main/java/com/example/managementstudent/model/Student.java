package com.example.managementstudent.model;

import java.util.Date;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private List<Certificate> certificateList;
    private String className;
    private String birthDay;
    private String imgStudent;

    public Student(String id, String name, List<Certificate> certificateList, String className, String birthDay, String imgStudent) {
        this.id = id;
        this.name = name;
        this.certificateList = certificateList;
        this.className = className;
        this.birthDay = birthDay;
        this.imgStudent = imgStudent;
    }

    public Student(String name, List<Certificate> certificateList, String className, String birthDay, String imgStudent) {
        this.name = name;
        this.certificateList = certificateList;
        this.className = className;
        this.birthDay = birthDay;
        this.imgStudent = imgStudent;
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
}

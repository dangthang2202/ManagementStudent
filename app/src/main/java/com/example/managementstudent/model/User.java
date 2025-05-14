package com.example.managementstudent.model;

public class User {

    private String id;
    private String name;
    private String position;
    private String status;
    private String linkImage;
    private String username;
    private String password;

    public User(String id, String name, String position, String status, String linkImage, String username, String password) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.status = status;
        this.linkImage = linkImage;
        this.username = username;
        this.password = password;
    }

    public User(String name, String position, String status, String linkImage, String username, String password) {
        this.name = name;
        this.position = position;
        this.status = status;
        this.linkImage = linkImage;
        this.username = username;
        this.password = password;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.pnb.example.application.data.entity;

import jakarta.persistence.Entity;

@Entity
public class UserManagement extends AbstractEntity {

    private Integer userId;
    private String userName;
    private String password;

    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}

package com.jobhub.components;

import com.jobhub.DataBase.JobSeekerConnection;
import com.jobhub.DataBase.RecruiterConnection;

import java.sql.Connection;

public class User {
    private String username;
    private final String role;
    private final Integer id;
    private final Connection connection;

    public void setUsername(String username) {
        this.username = username;
    }

    public User(String username, String role, Integer id) {
        this.username = username;
        this.role = role;
        this.connection = role.equals("job_seeker") ? JobSeekerConnection.getConnection() : RecruiterConnection.getConnection();
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public Integer getId() {
        return id;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getUsername() {
        return username;
    }

}

package com.jobhub.model;

import com.jobhub.DataBase.JobSeekerConnection;
import com.jobhub.DataBase.RecruiterConnection;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;

@Getter
public class User {
    @Setter
    private String username;
    private final String role;
    private final Integer id;
    private final Connection connection;

    public User(String username, String role, Integer id) {
        this.username = username;
        this.role = role;
        this.connection = role.equals("job_seeker") ? JobSeekerConnection.getConnection() : RecruiterConnection.getConnection();
        this.id = id;
    }

}

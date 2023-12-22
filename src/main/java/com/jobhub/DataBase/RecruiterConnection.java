package com.jobhub.DataBase;

import java.sql.Connection;
import java.sql.DriverAction;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RecruiterConnection {

    private static final String url = "jdbc:mysql://localhost:3306/job_hub_db";
    private static final String userName = "recruiter";
    private static final String password = "mysql804218$89742";

    private static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection(){
        return connection;
    }

    private RecruiterConnection()  {
    }
}

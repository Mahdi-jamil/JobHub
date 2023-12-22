package com.jobhub.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RootConnection {
    private static final String url = "jdbc:mysql://localhost:3306/job_hub_db";
    private static final String userName = "root";
    private static final String password = "root";

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

    private RootConnection()  {
    }
}

package com.homework.connection;

import com.homework.util.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager{
    public static final String URL = "db.url";
    public static final String DRAIVER = "db.driver";
    public static final String USERNAME = "db.username";
    public static final String PASSWORD = "db.password";

    public void loadDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не загружен");
        }
    }
    @Override
    public Connection getConnection() throws SQLException {
        loadDriver(PropertiesLoader.getProperties(DRAIVER));
        return DriverManager.getConnection(
                PropertiesLoader.getProperties(URL),
                PropertiesLoader.getProperties(USERNAME),
                PropertiesLoader.getProperties(PASSWORD)
        );
    }
}

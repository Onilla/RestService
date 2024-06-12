package com.homework.connection;

import com.homework.util.InitProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager{
    public void loadDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не загружен");
        }
    }
    @Override
    public Connection getConnection() throws SQLException {
        loadDriver(InitProperties.getProperties("db.driver"));
        return DriverManager.getConnection(
                InitProperties.getProperties("db.url"),
                InitProperties.getProperties("db.username"),
                InitProperties.getProperties("db.password")
        );
    }
}

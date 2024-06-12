package com.homework.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface ConnectionManager {


    Connection getConnection() throws SQLException;

}

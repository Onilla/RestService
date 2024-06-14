package com.homework;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.util.CreateSchemaSql;

public class Main {
    public static void main(String[] args) {
        ConnectionManager connectionManager = new ConnectionManagerImpl();
        CreateSchemaSql.createSqlScheme(connectionManager);
    }
}
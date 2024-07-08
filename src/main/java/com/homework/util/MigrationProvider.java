package com.homework.util;

import com.homework.connection.ConnectionManager;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MigrationProvider {
    private static final String SCHEME = "sql/schema.sql";
    private static String schemaSql;

    static {
        loadInitSQL();
    }

    private MigrationProvider() {
    }

    private static void loadInitSQL() {
        try (InputStream inFile = MigrationProvider.class.getClassLoader().getResourceAsStream(SCHEME)) {
            schemaSql = new String(inFile.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    public static void createSqlScheme(ConnectionManager connectionManager) {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(schemaSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

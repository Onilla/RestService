package com.homework.util;

import com.homework.connection.ConnectionManager;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateSchemaSql {
    private static final String SCHEME = "sql/schema.sql";
    private static final String DATA = "sql/data.sql";
    private static String schemaSql;
    private static String dataSql;

    static {
        loadInitSQL();

    }

    private CreateSchemaSql() {
    }

    public static void initSqlData(ConnectionManager connectionManager) {
        try (Connection connection = connectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(dataSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadInitSQL() {
        try (InputStream inFile = CreateSchemaSql.class.getClassLoader().getResourceAsStream(SCHEME)) {
            schemaSql = new String(inFile.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
        try (InputStream inFile = CreateSchemaSql.class.getClassLoader().getResourceAsStream(DATA)) {
            dataSql = new String(inFile.readAllBytes(), StandardCharsets.UTF_8);
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

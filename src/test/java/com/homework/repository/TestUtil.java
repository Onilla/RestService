package com.homework.repository;

import com.homework.util.PropertiesLoader;
import org.testcontainers.containers.PostgreSQLContainer;


public class TestUtil {
    public static PostgreSQLContainer<?> testUtil() {
        return new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("rest-service")
                .withUsername(PropertiesLoader.getProperties("db.username"))
                .withPassword(PropertiesLoader.getProperties("db.password"))
                .withInitScript("sql/schema.sql");
    }
}

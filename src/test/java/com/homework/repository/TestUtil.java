package com.homework.repository;

import com.homework.util.InitProperties;
import org.testcontainers.containers.PostgreSQLContainer;


public class TestUtil {
    public static PostgreSQLContainer<?> testUtil() {
        return new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("rest-service")
                .withUsername(InitProperties.getProperties("db.username"))
                .withPassword(InitProperties.getProperties("db.password"))
                .withInitScript("sql/schema.sql");
    }
}

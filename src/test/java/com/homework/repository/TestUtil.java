package com.homework.repository;

import org.testcontainers.containers.PostgreSQLContainer;


public class TestUtil {
    public static PostgreSQLContainer<?> testUtil() {
        return new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("rest-service")
                .withUsername("postgres")
                .withPassword("asdf4567hjkl");
    }
}

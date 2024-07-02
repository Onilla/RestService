package com.homework.repository;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.homework.util.InitProperties;
import org.testcontainers.containers.PostgreSQLContainer;


public class TestUtil {
    public static PostgreSQLContainer<?> testUtil() {
        return new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("rest-service")
                .withUsername(InitProperties.getProperties("db.username"))
                .withPassword(InitProperties.getProperties("db.password"))
                .withExposedPorts(5432)
                .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                        new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))
                ))
                .withInitScript("sql/schema.sql");
    }
}

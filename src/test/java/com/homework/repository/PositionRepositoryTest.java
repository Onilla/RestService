package com.homework.repository;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ContainerConnectionManager;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.repository.impl.CompanyRepositoryImpl;
import com.homework.repository.impl.PositionRepositoryImpl;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
class PositionRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = TestUtil.testUtil();


    ConnectionManager connectionManager = new ContainerConnectionManager(postgres.getJdbcUrl(),
            postgres.getUsername(),postgres.getPassword());

    private final PositionRepositoryImpl positionRepository = new PositionRepositoryImpl(connectionManager);

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void save() {
        List<User> users = new ArrayList<>();
        Position position = new Position(null, "Java Developer", users);
        Position savedPosition = positionRepository.save(position);
        Assertions.assertNotNull(savedPosition.getId());
        positionRepository.deleteById(savedPosition.getId());
    }

    @Test
    void delete() {
        List<User> usersList = new ArrayList<>();
        Position position = new Position(null, "Developer", usersList);
        Position savedPosition = positionRepository.save(position);
        boolean deleteResult = positionRepository.deleteById(savedPosition.getId());
        Assertions.assertTrue(deleteResult);
    }

    @Test
    void existById() {
        boolean existResult = positionRepository.existById(4L);
        Assertions.assertTrue(existResult);
    }

    @Test
    void update() {
        String nameForUpdate = "Developer";
        Position positionForUpdate = positionRepository.findById(1L).get();
        String nameBeforeUpdate = positionForUpdate.getName();
        positionForUpdate.setName(nameForUpdate);
        positionRepository.update(positionForUpdate);
        Position positionAfterUpdate = positionRepository.findById(1L).get();
        Assertions.assertNotEquals(nameForUpdate, nameBeforeUpdate);
        Assertions.assertEquals(nameForUpdate, positionAfterUpdate.getName());
    }

    @Test
    void findById() {
        long id = 4L;
        Position position = positionRepository.findById(id).get();
        Assertions.assertNotNull(position);
    }

    @Test
    void findAll() {
        int expectedSize = 4;
        int resultSize = positionRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

}

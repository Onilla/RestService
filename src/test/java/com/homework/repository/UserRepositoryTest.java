package com.homework.repository;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ContainerConnectionManager;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = TestUtil.testUtil();

    ConnectionManager connectionManager = new ContainerConnectionManager(postgres.getJdbcUrl(),
            postgres.getUsername(),postgres.getPassword());

    private final UserRepositoryImpl userRepository = new UserRepositoryImpl(connectionManager);

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
        Company company = new Company(1L, "Yandex");
        List<Position> positions = new ArrayList<>();
        User user = new User(null, "Виктор", "Комаров", company, positions);
        User savedUser = userRepository.save(user);
        Assertions.assertNotNull(savedUser.getId());
    }

    @Test
    void delete() {
        boolean deleteResult = userRepository.deleteById(1L);
        Assertions.assertTrue(deleteResult);
    }

    @Test
    void update() {
        String nameForUpdate = "Валентинов";
        User userForUpdate = userRepository.findById(4L).get();
        String nameBeforeUpdate = userForUpdate.getLastname();
        userForUpdate.setLastname(nameForUpdate);
        userRepository.update(userForUpdate);
        User userAfterUpdate = userRepository.findById(4L).get();
        Assertions.assertNotEquals(nameForUpdate, nameBeforeUpdate);
        Assertions.assertEquals(nameForUpdate, userAfterUpdate.getLastname());
    }

    @Test
    void existById() {
        boolean existResult = userRepository.existById(3L);
        Assertions.assertTrue(existResult);
    }

    @Test
    void findById() {
        long id = 3L;
        User user = userRepository.findById(id).get();
        Assertions.assertNotNull(user);
    }

    @Test
    void findAll() {
        int expectedSize = 3;
        int resultSize = userRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

}

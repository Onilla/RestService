package com.homework.repository;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.repository.impl.UserRepositoryImpl;
import com.homework.util.CreateSchemaSql;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
class UserRepositoryTest {

    private final Repository<User, Long> userRepository = new UserRepositoryImpl();
    static ConnectionManager connectionManager = new ConnectionManagerImpl();


    @Container
    private static final PostgreSQLContainer<?> postgres = TestUtil.testUtil();

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void setUp() {
        CreateSchemaSql.createSqlScheme(connectionManager);
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
        long id = 1L;
        User user = userRepository.findById(id).get();
        Assertions.assertNotNull(user);
    }

    @Test
    void findAll() {
        int expectedSize = 4;
        int resultSize = userRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

}

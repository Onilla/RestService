package com.homework.repository;

import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.repository.impl.PositionRepositoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
@Testcontainers
class PositionRepositoryTest {

    private final Repository<Position, Long> positionRepository = new PositionRepositoryImpl();

    @Container
    private static final PostgreSQLContainer<?> postgres = TestUtil.testUtil();

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
        Long id = null;
        String positionName = "Java Developer";
        List<User> users = new ArrayList<>();
        Position position = new Position(id, positionName, users);
        Position savedPosition = positionRepository.save(position);
        Assertions.assertNotNull(savedPosition.getId());
        positionRepository.deleteById(savedPosition.getId());
    }

    @Test
    void delete() {
        Long id = null;
        String positionName = "Developer";
        List<User> usersList = new ArrayList<>();
        Position position = new Position(id, positionName, usersList);
        Position savedPosition = positionRepository.save(position);
        boolean deleteResult = positionRepository.deleteById(savedPosition.getId());
        Assertions.assertTrue(deleteResult);
    }

    @Test
    void existById() {
        boolean existResult = positionRepository.existById(14L);
        Assertions.assertTrue(existResult);
    }

    @Test
    void updateCompany() {
        String nameForUpdate = "Developer";
        Position positionForUpdate = positionRepository.findById(27L).get();
        String nameBeforeUpdate = positionForUpdate.getName();
        positionForUpdate.setName(nameForUpdate);
        positionRepository.update(positionForUpdate);
        Position positionAfterUpdate = positionRepository.findById(27L).get();
        Assertions.assertNotEquals(nameForUpdate, nameBeforeUpdate);
        Assertions.assertEquals(nameForUpdate, positionAfterUpdate.getName());
    }

    @Test
    void findById() {
        long id = 14L;
        Position position = positionRepository.findById(id).get();
        Assertions.assertNotNull(position);
    }

    @Test
    void findAllPositions() {
        int expectedSize = 6;
        int resultSize = positionRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

}

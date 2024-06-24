package com.homework.repository;

import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class UserRepositoryTest {

    private final Repository<User, Long> userRepository = new UserRepositoryImpl();

    @Test
    void save() {
        Long id = null;
        String firstname = "Виктор";
        String lastname = "Комаров";
        Company company = new Company(1L, "Yandex");
        List<Position> positions = new ArrayList<>();
        User user = new User(id, firstname, lastname, company, positions);
        User savedUser = userRepository.save(user);
        Assertions.assertNotNull(savedUser.getId());
        userRepository.deleteById(savedUser.getId());
    }

    @Test
    void delete() {
        Long id = null;
        String firstname = "Ivan";
        String lastname = "Ivanov";
        Company company = new Company(1L, "Yandex");
        List<Position> positions = new ArrayList<>();
        User user = new User(id, firstname, lastname, company, positions);
        User savedUser = userRepository.save(user);
        boolean deleteResult = userRepository.deleteById(savedUser.getId());
        Assertions.assertTrue(deleteResult);
    }

    @Test
    void updateCompany() {
        String nameForUpdate = "Валентин";
        User userForUpdate = userRepository.findById(22L).get();
        String nameBeforeUpdate = userForUpdate.getLastname();
        userForUpdate.setLastname(nameForUpdate);
        userRepository.update(userForUpdate);
        User userAfterUpdate = userRepository.findById(22L).get();
        Assertions.assertNotEquals(nameForUpdate, nameBeforeUpdate);
        Assertions.assertEquals(nameForUpdate, userAfterUpdate.getLastname());
    }

    @Test
    void existById() {
        boolean existResult = userRepository.existById(5L);
        Assertions.assertTrue(existResult);
    }

    @Test
    void findById() {
        long id = 5L;
        User user = userRepository.findById(id).get();
        Assertions.assertNotNull(user);
    }

    @Test
    void findAllPositions() {
        int expectedSize = 4;
        int resultSize = userRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }
}

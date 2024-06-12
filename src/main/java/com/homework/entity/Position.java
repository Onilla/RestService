package com.homework.entity;

import com.homework.repository.UserRepository;
import com.homework.repository.impl.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class Position {

    private Long id;
    private String name;
    private List<User> users;
    private final UserRepository userRepository = new UserRepositoryImpl();

    public Position(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Position(Long id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public Position() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = userRepository.findUsersByPositionId(this.id);
        }
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

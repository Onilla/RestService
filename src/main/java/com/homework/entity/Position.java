package com.homework.entity;

import java.util.List;

public class Position {

    private Long id;
    private String name;
    private List<User> users;

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

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

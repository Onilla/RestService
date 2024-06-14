package com.homework.dto;

import java.util.List;

public class CompanyOutGoingDto {
    private Long id;
    private String name;
    private List<String> users;

    public CompanyOutGoingDto() {
    }

    public CompanyOutGoingDto(Long id, String name, List<String> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }
    public CompanyOutGoingDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}

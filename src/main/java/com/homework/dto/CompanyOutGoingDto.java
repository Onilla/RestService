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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getUsers() {
        return users;
    }
}

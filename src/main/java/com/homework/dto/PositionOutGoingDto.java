package com.homework.dto;

import java.util.List;

public class PositionOutGoingDto {
    private Long id;
    private String name;
    private List<UserOutGoingDto> userList;

    public PositionOutGoingDto(Long id, String name, List<UserOutGoingDto> userList) {
        this.id = id;
        this.name = name;
        this.userList = userList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<UserOutGoingDto> getUserList() {
        return userList;
    }
}

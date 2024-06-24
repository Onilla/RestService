package com.homework.dto;

import java.util.List;

public class PositionOutGoingDto {
    private Long id;
    private String name;
    private List<String> userList;

    public PositionOutGoingDto(Long id, String name, List<String> userList) {
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

    public List<String> getUserList() {
        return userList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}

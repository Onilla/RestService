package com.homework.dto;

import java.util.List;

public class PositionIncomingDto {

    private String name;

    private List<Long> userIds;

    public PositionIncomingDto() {
    }

    public PositionIncomingDto(String name, List<Long> userIds) {
        this.name = name;
        this.userIds = userIds;
    }

    public PositionIncomingDto(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}

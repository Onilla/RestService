package com.homework.dto;

import java.util.List;

public class PositionUpdateDto {
    private Long Id;
    private String name;
    private List<Long> userIds;

    public PositionUpdateDto(Long id, String name, List<Long> userIds) {
        Id = id;
        this.name = name;
        this.userIds = userIds;
    }

    public PositionUpdateDto(){}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}

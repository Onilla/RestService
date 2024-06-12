package com.homework.dto;

import com.homework.entity.Position;

public class PositionIncomingDto {

    private String name;

    public PositionIncomingDto(String name) {
        this.name = name;
    }
    public PositionIncomingDto(){
    }

    public String getName() {
        return name;
    }
}

package com.homework.dto;

public class CompanyOutGoingDto {
    private Long id;
    private String name;

    public CompanyOutGoingDto() {
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
}

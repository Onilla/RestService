package com.homework.dto;

public class CompanyUpdateDto {
    private Long id;
    private String name;

    public CompanyUpdateDto() {
    }

    public CompanyUpdateDto(Long id, String name) {
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

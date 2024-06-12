package com.homework.dto;

import com.homework.entity.Company;

import java.util.List;

public class UserUpdateDto {


    private Long id;
    private String firstname;
    private String lastname;
    private Company company;
    private List<PositionUpdateDto> positionUpdateDtoList;
    public UserUpdateDto() {
    }

    public UserUpdateDto(Long id, String firstname, String lastname, Company company, List<PositionUpdateDto> positionUpdateDtoList) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
        this.positionUpdateDtoList=positionUpdateDtoList;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Company getCompany() {
        return company;
    }

    public List<PositionUpdateDto> getPositionUpdateDtoList() {
        return positionUpdateDtoList;
    }
}

package com.homework.dto;

import com.homework.entity.Company;

public class UserFullOutGoingDto {

    private Long id;
    private String firstname;
    private String lastname;
    private CompanyOutGoingDto company;

    public UserFullOutGoingDto() {
    }

    public UserFullOutGoingDto(Long id, String firstname, String lastname, CompanyOutGoingDto company) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
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

    public CompanyOutGoingDto getCompany() {
        return company;
    }
}

package com.homework.dto;

import com.homework.entity.Company;

public class UserIncomingDto {
    private String firstname;
    private String lastname;
    private Company company;

    public UserIncomingDto() {
    }

    public UserIncomingDto(String firstname, String lastname, Company company) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
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
}

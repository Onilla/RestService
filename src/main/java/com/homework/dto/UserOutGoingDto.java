package com.homework.dto;

import java.util.List;

public class UserOutGoingDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String companyName;
    private List<String> positions;

    public UserOutGoingDto() {
    }

    public UserOutGoingDto(Long id, String firstname, String lastname, String companyId, List<String> positions) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.companyName = companyId;
        this.positions = positions;
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

    public String getCompanyName() {
        return companyName;
    }

    public List<String> getPositions() {
        return positions;
    }
}

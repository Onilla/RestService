package com.homework.dto;

import java.util.List;

public class UserUpdateDto {


    private Long id;
    private String firstname;
    private String lastname;
    private Long companyId;
    private List<Long> positions;

    public UserUpdateDto() {
    }

    public UserUpdateDto(Long id, String firstname, String lastname, Long companyId, List<Long> positions) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.companyId = companyId;
        this.positions=positions;
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

    public Long getCompanyId() {
        return companyId;
    }

    public List<Long> getPositions() {
        return positions;
    }
}

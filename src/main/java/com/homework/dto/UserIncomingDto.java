package com.homework.dto;

import java.util.List;

public class UserIncomingDto {
    private String firstname;
    private String lastname;
    private Long companyId;
    private List<Long> positions;

    public UserIncomingDto() {
    }

    public UserIncomingDto(String firstname, String lastname, Long companyId, List<Long> positions) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.companyId = companyId;
        this.positions = positions;
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

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setPositions(List<Long> positions) {
        this.positions = positions;
    }
}

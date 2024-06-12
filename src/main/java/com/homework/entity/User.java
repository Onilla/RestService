package com.homework.entity;

import java.util.List;

public class User {

    private Long id;
    private String firstname;
    private String lastname;
    private Company company;
    private List<Position> positions;

    public User(Long id, String firstname, String lastname, Company company, List<Position> positions) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company = company;
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

    public Company getCompany() {
        return company;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    public Long getCompanyId() {
        return this.company.getId();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCompanyId(Long id) {
        this.company.setId(id);
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}

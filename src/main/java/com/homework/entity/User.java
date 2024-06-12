package com.homework.entity;

import com.homework.repository.UserRepository;
import com.homework.repository.impl.UserRepositoryImpl;


import java.util.List;


public class User {

    private Long id;
    private String firstname;
    private String lastname;
    private Company company;
    private List<Position> positions;
    private final UserRepository userRepository = new UserRepositoryImpl();
    public User() {
    }

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


    public List<Position> getPositions() {
        return positions;
    }
//    public void setPositions(List<Position> positions) {
//        this.positions = positions;
//    }
}

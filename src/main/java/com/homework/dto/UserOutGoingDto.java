package com.homework.dto;

public class UserOutGoingDto {
    private Long id;
    private String firstname;
    private String lastname;

    public UserOutGoingDto() {
    }

    public UserOutGoingDto(Long id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
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
}

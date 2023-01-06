package com.zoomania.zoomania.model.dto;

public class UserDetailsDTO {
    private String username;
    private String email;
    private String phone;
    private String age;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return username;
    }

    public UserDetailsDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDetailsDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserDetailsDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAge() {
        return age;
    }

    public UserDetailsDTO setAge(String age) {
        this.age = age;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserDetailsDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserDetailsDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}

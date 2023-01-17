package com.zoomania.zoomania.model.dto.user;

import com.zoomania.zoomania.util.validation.UniqueUserEmail;
import com.zoomania.zoomania.util.validation.UniqueUsername;

import javax.validation.constraints.*;

public class UpdateUserDTO {
    @NotEmpty(message = "Username can't be empty.")
    @Size(min = 2,max = 20,message = "Must be between 2 and 20 symbols.")
    @UniqueUsername(message = "Username should be unique.")
    private String username;
    @NotEmpty(message = "First Name is required.")
    @Size(min = 2,max = 20,message = "First Name must be between 2 nad 20 symbols.")
    private String firstName;
    @NotEmpty(message = "User email should be provided.")
    @Email(message = "User email should be valid.")
    @UniqueUserEmail(message = "User email should be unique.")
    private String email;
    @NotEmpty(message = "Last Name is required.")
    @Size(min = 2,max = 20,message = "Last Name must be between 2 nad 20 symbols.")
    private String lastName;
    @NotEmpty(message = "Phone is required.")
    @Pattern(regexp = "[0-9]\\d{1,20}",message = "Phone is invalid.")
    private String phone;
    @Min(value = 10,message = "Minimum age is 10.")
    @Max(value = 99,message = "Maximum age is 99.")
    private Integer age;
    private boolean isActive;
    private boolean isAdmin;


    public String getUsername() {
        return username;
    }

    public UpdateUserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UpdateUserDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateUserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UpdateUserDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UpdateUserDTO setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UpdateUserDTO setAge(Integer age) {
        this.age = age;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public UpdateUserDTO setActive(boolean active) {
        isActive = active;
        return this;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public UpdateUserDTO setAdmin(boolean admin) {
        isAdmin = admin;
        return this;
    }
}

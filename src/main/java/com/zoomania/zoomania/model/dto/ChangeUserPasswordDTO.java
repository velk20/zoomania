package com.zoomania.zoomania.model.dto;

import com.zoomania.zoomania.util.validation.FieldMatch;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@FieldMatch(
        first = "newPassword",
        second = "confirmPassword",
        message = "Passwords do not match"
)

public class ChangeUserPasswordDTO {
    private String username;
    @NotEmpty(message = "Password is required.")
    @Size(min = 5,message = "Password must be at least 5 symbols.")
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public ChangeUserPasswordDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public ChangeUserPasswordDTO setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public ChangeUserPasswordDTO setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public ChangeUserPasswordDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }
}

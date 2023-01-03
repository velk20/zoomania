package com.zoomania.zoomania.model.entity;


import com.zoomania.zoomania.model.enums.UserRoleEnum;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRoleEntity extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private UserRoleEnum userRoleEnum;

    public UserRoleEnum getUserRoleEnum() {
        return userRoleEnum;
    }

    public UserRoleEntity setUserRoleEnum(UserRoleEnum userRoleEnum) {
        this.userRoleEnum = userRoleEnum;
        return this;
    }
    @Override
    public String toString() {
        return "UserRoleEntity{" +
                ", userRoleEnum=" + userRoleEnum +
                '}';
    }
}

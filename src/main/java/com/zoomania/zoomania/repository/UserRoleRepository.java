package com.zoomania.zoomania.repository;

import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity,Long> {
    UserRoleEntity findByUserRoleEnum(UserRoleEnum userRoleEnum);
}

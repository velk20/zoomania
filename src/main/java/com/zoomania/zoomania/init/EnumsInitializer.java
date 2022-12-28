package com.zoomania.zoomania.init;

import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnumsInitializer implements CommandLineRunner {
    private final UserRoleRepository userRoleRepository;

    public EnumsInitializer(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUserRolesInDatabase();
    }

    public void seedUserRolesInDatabase() {
        if (userRoleRepository.count() <= 0) {
            Arrays.stream(UserRoleEnum.values())
                    .forEach(
                            r->this.userRoleRepository.save(
                                    new UserRoleEntity()
                                    .setUserRoleEnum(r))
                    );
        }
    }
}

package com.zoomania.zoomania.init;

import com.zoomania.zoomania.model.entity.CategoryEntity;
import com.zoomania.zoomania.model.entity.UserRoleEntity;
import com.zoomania.zoomania.model.enums.CategoryEnum;
import com.zoomania.zoomania.model.enums.UserRoleEnum;
import com.zoomania.zoomania.repository.CategoryRepository;
import com.zoomania.zoomania.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnumsInitializer implements CommandLineRunner {
    private final UserRoleRepository userRoleRepository;
    private final CategoryRepository categoryRepository;

    public EnumsInitializer(UserRoleRepository userRoleRepository, CategoryRepository categoryRepository) {
        this.userRoleRepository = userRoleRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUserRolesInDatabase();
        seedCategoriesInDatabase();
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

    public void seedCategoriesInDatabase() {
        if (categoryRepository.count() <= 0) {
            Arrays.stream(CategoryEnum.values())
                    .forEach(
                            r->this.categoryRepository.save(
                                    new CategoryEntity()
                                            .setName(r))
                    );
        }
    }
}

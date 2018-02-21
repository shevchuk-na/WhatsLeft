package ru.whatsleft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.whatsleft.domain.security.Role;
import ru.whatsleft.domain.security.RoleEnum;
import ru.whatsleft.repository.RoleRepository;
import ru.whatsleft.service.UserService;

@SpringBootApplication
public class WhatsleftApplication implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public WhatsleftApplication(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WhatsleftApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Role roleUser = roleRepository.findByName(RoleEnum.ROLE_USER);
        if (roleUser == null) {
            roleUser = new Role();
            roleUser.setName(RoleEnum.ROLE_USER);
            roleUser.setRoleId(1);
            roleRepository.save(roleUser);
        }
        Role roleAdmin = roleRepository.findByName(RoleEnum.ROLE_ADMIN);
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setName(RoleEnum.ROLE_ADMIN);
            roleAdmin.setRoleId(2);
            roleRepository.save(roleAdmin);
        }
        Role roleLeader = roleRepository.findByName(RoleEnum.ROLE_LEADER);
        if (roleLeader == null) {
            roleLeader = new Role();
            roleLeader.setName(RoleEnum.ROLE_LEADER);
            roleLeader.setRoleId(3);
            roleRepository.save(roleLeader);
        }
    }
}

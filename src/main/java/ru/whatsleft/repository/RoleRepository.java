package ru.whatsleft.repository;

import org.springframework.data.repository.CrudRepository;
import ru.whatsleft.domain.security.Role;
import ru.whatsleft.domain.security.RoleEnum;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(RoleEnum name);
}

package ru.whatsleft.repository;

import org.springframework.data.repository.CrudRepository;
import ru.whatsleft.domain.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findById(Long id);

    User findByUsername(String username);

    List<User> findByLeader(User leader);
}

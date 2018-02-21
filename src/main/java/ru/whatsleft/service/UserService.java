package ru.whatsleft.service;

import ru.whatsleft.domain.User;
import ru.whatsleft.domain.security.PasswordResetToken;

import java.util.List;

public interface UserService {

    PasswordResetToken getPasswordResetToken(final String token);

    void createPasswordResetTokenForUser(final User user, final String token);

    User findById(Long id);

    User findByUsername(String username);

    List<User> findByLeader(User leader);

    User createUser(User user) throws Exception;

    User save(User user);

}

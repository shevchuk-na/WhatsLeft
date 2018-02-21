package ru.whatsleft.service;

import ru.whatsleft.domain.Category;
import ru.whatsleft.domain.User;

import java.util.List;

public interface CategoryService {

    Category save(Category category);

    List<Category> findByTeamLeaderId(Long id);

    Category findByNameAndTeamLeaderId(String name, User user);

    Category findById(Long id);

    void delete(Long id);
}

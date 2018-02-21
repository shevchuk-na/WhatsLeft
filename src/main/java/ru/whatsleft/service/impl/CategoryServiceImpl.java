package ru.whatsleft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.whatsleft.domain.Category;
import ru.whatsleft.domain.User;
import ru.whatsleft.repository.CategoryRepository;
import ru.whatsleft.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findByTeamLeaderId(Long id) {
        return categoryRepository.findByTeamLeaderId(id);
    }

    @Override
    public Category findByNameAndTeamLeaderId(String name, User user) {
        return categoryRepository.findByNameAndTeamLeaderId(name, user.getId());
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.delete(id);
    }
}

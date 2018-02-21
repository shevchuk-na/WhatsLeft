package ru.whatsleft.repository;

import org.springframework.data.repository.CrudRepository;
import ru.whatsleft.domain.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findByTeamLeaderId(Long id);

    Category findByNameAndTeamLeaderId(String name, Long id);

    Category findById(Long id);
}

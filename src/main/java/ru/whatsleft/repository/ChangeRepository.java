package ru.whatsleft.repository;

import org.springframework.data.repository.CrudRepository;
import ru.whatsleft.domain.Change;

import java.util.List;

public interface ChangeRepository extends CrudRepository<Change, Long> {

    Change findTopByUserIdOrderByIdDesc(Long id);

    List<Change> findTop10ByProductIdOrderByIdDesc(Long id);
}

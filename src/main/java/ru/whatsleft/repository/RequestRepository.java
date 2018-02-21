package ru.whatsleft.repository;

import org.springframework.data.repository.CrudRepository;
import ru.whatsleft.domain.Request;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {

    List<Request> findAll();
}

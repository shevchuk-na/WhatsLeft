package ru.whatsleft.service;

import ru.whatsleft.domain.Change;

import java.util.List;

public interface ChangeService {

    Change save(Change change);

    Change findLastByUserId(Long id);

    List<Change> findLast10ByProductId(Long id);

    void delete(Change lastChange);
}

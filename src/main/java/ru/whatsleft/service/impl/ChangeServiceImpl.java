package ru.whatsleft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.whatsleft.domain.Change;
import ru.whatsleft.repository.ChangeRepository;
import ru.whatsleft.service.ChangeService;

import java.util.List;

@Service
public class ChangeServiceImpl implements ChangeService {

    private final ChangeRepository changeRepository;

    @Autowired
    public ChangeServiceImpl(ChangeRepository changeRepository) {
        this.changeRepository = changeRepository;
    }

    @Override
    public Change save(Change change) {
        return changeRepository.save(change);
    }

    @Override
    public Change findLastByUserId(Long id) {
        return changeRepository.findTopByUserIdOrderByIdDesc(id);
    }

    @Override
    public void delete(Change lastChange) {
        changeRepository.delete(lastChange.getId());
    }

    @Override
    public List<Change> findLast10ByProductId(Long id) {
        return changeRepository.findTop10ByProductIdOrderByIdDesc(id);
    }
}

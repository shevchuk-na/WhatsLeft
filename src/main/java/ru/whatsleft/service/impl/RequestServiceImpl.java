package ru.whatsleft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.whatsleft.domain.Request;
import ru.whatsleft.repository.RequestRepository;
import ru.whatsleft.service.RequestService;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Request save(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public List<Request> findAllRequests() {
        return requestRepository.findAll();
    }
}

package ru.whatsleft.service;

import ru.whatsleft.domain.Request;

import java.util.List;

public interface RequestService {

    Request save(Request request);

    List<Request> findAllRequests();
}

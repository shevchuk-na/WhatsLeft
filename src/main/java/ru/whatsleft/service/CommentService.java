package ru.whatsleft.service;

import ru.whatsleft.domain.Comment;

public interface CommentService {

    Comment save(Comment comment);

    Comment findById(Long id);

    void delete(Long id);
}

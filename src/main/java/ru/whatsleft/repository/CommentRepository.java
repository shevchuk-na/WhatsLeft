package ru.whatsleft.repository;

import org.springframework.data.repository.CrudRepository;
import ru.whatsleft.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}

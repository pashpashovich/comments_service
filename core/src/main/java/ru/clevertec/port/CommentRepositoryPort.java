package ru.clevertec.port;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.entity.Comment;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepositoryPort {
    Comment save(Comment comment);

    Optional<Comment> findById(UUID id);

    Page<Comment> findByNewsId(UUID newsId, Pageable pageable);

    void deleteById(UUID id);

    Page<Comment> findAll(Pageable pageable);
}

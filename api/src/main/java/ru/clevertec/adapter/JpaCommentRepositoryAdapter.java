package ru.clevertec.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.entity.Comment;
import ru.clevertec.port.CommentRepositoryPort;
import ru.clevertec.repository.CommentRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class JpaCommentRepositoryAdapter implements CommentRepositoryPort {

    private final CommentRepository commentRepository;

    public JpaCommentRepositoryAdapter(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(UUID id) {
        return commentRepository.findById(id);
    }

    @Override
    public Page<Comment> findByNewsId(UUID newsId, Pageable pageable) {
        return commentRepository.findByNewsId(newsId, pageable);
    }

    @Override
    public void deleteById(UUID id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }
}

package ru.clevertec.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.dto.CommentDto;
import ru.clevertec.entity.Comment;
import ru.clevertec.mapper.CommentsMapper;
import ru.clevertec.port.CommentRepositoryPort;

import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepositoryPort repository;
    private final CommentsMapper commentsMapper;

    public CommentService(CommentRepositoryPort repository, CommentsMapper commentsMapper) {
        this.repository = repository;
        this.commentsMapper = commentsMapper;
    }

    public Page<CommentDto> getAllComments(Pageable pageable) {
        return commentsMapper.toDtoList(repository.findAll(pageable));
    }

    public Page<CommentDto> getCommentsByNewsId(UUID newsId, Pageable pageable) {
        return commentsMapper.toDtoList(repository.findByNewsId(newsId, pageable));
    }

    public CommentDto createComment(Comment comment) {
        return commentsMapper.toDto(repository.save(comment));
    }

    public Optional<Comment> getCommentById(UUID id) {
        return repository.findById(id);
    }

    public void deleteComment(UUID id) {
        repository.deleteById(id);
    }
}

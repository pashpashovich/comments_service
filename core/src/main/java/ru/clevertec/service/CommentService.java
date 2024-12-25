package ru.clevertec.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.dto.CommentCreateRequest;
import ru.clevertec.dto.CommentDto;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.mapper.CommentMapper;
import ru.clevertec.port.CommentRepositoryPort;
import ru.clevertec.port.NewsServicePort;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepositoryPort repository;
    private final CommentMapper commentsMapper;
    private final NewsServicePort newsServicePort;

    public Page<CommentDto> getAllComments(Pageable pageable) {
        return commentsMapper.toDtoList(repository.findAll(pageable));
    }

    public Page<CommentDto> getCommentsByNewsId(UUID newsId, Pageable pageable) {
        return commentsMapper.toDtoList(repository.findByNewsId(newsId, pageable));
    }

    public CommentDto createComment(CommentCreateRequest comment) {
        newsServicePort.getNewsById(comment.getNewsId());
        return commentsMapper.toDto(repository.save(commentsMapper.toDomain(comment)));
    }

    public CommentDto getCommentById(UUID id) {
        return commentsMapper.toDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с таким ID не найден")));
    }

    public void deleteComment(UUID id) {
        repository.deleteById(id);
    }
}

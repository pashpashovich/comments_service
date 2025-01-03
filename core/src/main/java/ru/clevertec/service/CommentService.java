package ru.clevertec.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.clevertec.cache.Cache;
import ru.clevertec.domain.Comment;
import ru.clevertec.dto.CommentCreateRequest;
import ru.clevertec.dto.CommentDto;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.mapper.CommentMapper;
import ru.clevertec.port.CommentRepositoryPort;
import ru.clevertec.port.NewsServicePort;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepositoryPort repository;
    private final CommentMapper commentsMapper;
    private final NewsServicePort newsServicePort;
    private final Cache<UUID, Comment> cache;

    public Page<CommentDto> getAllComments(Pageable pageable) {
        return commentsMapper.toDtoList(repository.findAll(pageable));
    }

    public Page<CommentDto> getCommentsByNewsId(UUID newsId, Pageable pageable) {
        newsServicePort.getNewsById(newsId);
        return commentsMapper.toDtoList(repository.findByNewsId(newsId, pageable));
    }

    public CommentDto createComment(CommentCreateRequest comment) {
        newsServicePort.getNewsById(comment.getNewsId());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Comment comment1 = commentsMapper.toDomain(comment);
        comment1.setUsername(username);
        Comment comment2 = repository.save(comment1);
        cache.put(comment2.getId(), comment2);
        return commentsMapper.toDto(comment2);
    }

    public CommentDto getCommentById(UUID id) {
        if (cache.contains(id)) {
            return commentsMapper.toDto(cache.get(id));
        }
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с таким ID не найден"));
        cache.put(comment.getId(),comment);
        return commentsMapper.toDto(comment);
    }

    public CommentDto updateComment(UUID id, CommentCreateRequest updateRequest) throws AccessDeniedException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с таким ID не найден"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        if (!comment.getUsername().equals(username) && !isAdmin)
            throw new AccessDeniedException("Вы не имеете права изменять этот комментарий!");
        comment.setText(updateRequest.getText());
        comment.setUsername(username);
        comment.setNewsId(updateRequest.getNewsId());
        CommentDto updatedComment = commentsMapper.toDto(repository.save(comment));

        cache.put(id, comment);

        return updatedComment;
    }

    public void deleteComment(UUID id) throws AccessDeniedException {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с таким ID не найден"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        if (!comment.getUsername().equals(username) && !isAdmin)
            throw new AccessDeniedException("Вы не имеете права изменять этот комментарий!");
        repository.deleteById(id);
        cache.remove(id);
    }
}

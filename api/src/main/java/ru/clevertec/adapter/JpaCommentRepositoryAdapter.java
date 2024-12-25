package ru.clevertec.adapter;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.clevertec.domain.Comment;
import ru.clevertec.entity.CommentEntity;
import ru.clevertec.mapper.CommentsDomainMapper;
import ru.clevertec.port.CommentRepositoryPort;
import ru.clevertec.repository.CommentRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaCommentRepositoryAdapter implements CommentRepositoryPort {

    private final CommentRepository commentRepository;
    private final CommentsDomainMapper commentsDomainMapper;

    @Override
    public Comment save(Comment comment) {
        return commentsDomainMapper.toDomain(commentRepository.save(commentsDomainMapper.toEntity(comment)));
    }

    @Override
    public Optional<Comment> findById(UUID id) {
        return commentRepository.findById(id)
                .map(commentsDomainMapper::toDomain);
    }

    @Override
    public Page<Comment> findByNewsId(UUID newsId, Pageable pageable) {
        return commentsDomainMapper.toDomainList(commentRepository.findByNewsId(newsId, pageable));
    }

    @Override
    public void deleteById(UUID id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> findAll(Pageable pageable) {
        return commentsDomainMapper.toDomainList(commentRepository.findAll(pageable));
    }
}

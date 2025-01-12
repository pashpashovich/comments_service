package ru.clevertec.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.clevertec.cache.Cache;
import ru.clevertec.domain.Comment;
import ru.clevertec.domain.CommentCreateRequestFromDto;
import ru.clevertec.domain.CommentFromDto;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.mapper.CommentMapper;
import ru.clevertec.port.CommentRepositoryPort;
import ru.clevertec.port.NewsServicePort;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServiceTest {

    @Mock
    private CommentRepositoryPort repository;

    @Mock
    private CommentMapper commentsMapper;

    @Mock
    private NewsServicePort newsServicePort;

    @Mock
    private Cache<UUID, Comment> cache;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentFromDto commentFromDto;
    private CommentCreateRequestFromDto commentCreateRequestFromDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("author");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        comment = new Comment(UUID.randomUUID(), "Some comment text", "author", LocalDateTime.now(), UUID.randomUUID());
        commentFromDto = new CommentFromDto(comment.getId(), comment.getText(), comment.getUsername(), comment.getCreatedAt());
        commentCreateRequestFromDto = new CommentCreateRequestFromDto(comment.getText(), comment.getNewsId());
    }

    @Test
    void shouldGetAllComments() {
        // given
        Page<Comment> commentsPage = new PageImpl<>(Collections.singletonList(comment));
        when(repository.findAll(any(Pageable.class))).thenReturn(commentsPage);
        when(commentsMapper.toDtoList(any(Page.class))).thenReturn(new PageImpl<>(List.of(commentFromDto)));
        //when
        Page<CommentFromDto> result = commentService.getAllComments(Pageable.unpaged());
        //then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(commentFromDto, result.getContent().get(0));
    }

    @Test
    void shouldGetCommentsByNewsId() {
        // given
        UUID newsId = UUID.randomUUID();
        Page<Comment> commentsPage = new PageImpl<>(Collections.singletonList(comment));
        when(newsServicePort.getNewsById(newsId)).thenReturn(null);
        when(repository.findByNewsId(newsId, Pageable.unpaged())).thenReturn(commentsPage);
        when(commentsMapper.toDtoList(any(Page.class))).thenReturn(new PageImpl<>(List.of(commentFromDto)));
        //when
        Page<CommentFromDto> result = commentService.getCommentsByNewsId(newsId, Pageable.unpaged());
        //then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(commentFromDto, result.getContent().get(0));
    }

    @Test
    void shouldCreateComment() {
        // given
        when(newsServicePort.getNewsById(commentCreateRequestFromDto.getNewsId())).thenReturn(null);
        when(commentsMapper.toDomain(commentCreateRequestFromDto)).thenReturn(comment);
        when(repository.save(any(Comment.class))).thenReturn(comment);
        when(commentsMapper.toDto(any(Comment.class))).thenReturn(commentFromDto);
        //when
        CommentFromDto result = commentService.createComment(commentCreateRequestFromDto);
        //then
        assertNotNull(result);
        assertEquals(commentFromDto, result);
        verify(repository, times(1)).save(any(Comment.class));
    }

    @Test
    void shouldGetCommentById() {
        // given
        UUID id = comment.getId();
        when(cache.contains(id)).thenReturn(true);
        when(cache.get(id)).thenReturn(comment);
        when(commentsMapper.toDto(any(Comment.class))).thenReturn(commentFromDto);
        //when
        CommentFromDto result = commentService.getCommentById(id);
        //then
        assertNotNull(result);
        assertEquals(commentFromDto, result);
        verify(repository, never()).findById(any(UUID.class));
    }

    @Test
    void shouldUpdateComment() throws AccessDeniedException {
        // given
        UUID id = comment.getId();
        String updatedText = "Updated text";
        CommentCreateRequestFromDto updateRequest = new CommentCreateRequestFromDto(updatedText, comment.getNewsId());

        when(repository.findById(id)).thenReturn(Optional.of(comment));
        when(newsServicePort.getNewsById(comment.getNewsId())).thenReturn(null);
        when(repository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(commentsMapper.toDto(any(Comment.class))).thenReturn(new CommentFromDto(comment.getId(), updatedText, comment.getUsername(), comment.getCreatedAt()));

        // when
        CommentFromDto result = commentService.updateComment(id, updateRequest);

        // then
        assertNotNull(result);
        assertEquals(updatedText, result.getText());
        verify(repository, times(1)).save(any(Comment.class));
        assertEquals(updatedText, comment.getText());
    }

    @Test
    void shouldUpdateCommentAccessDenied() {
        // given
        Comment comment2 = new Comment(UUID.randomUUID(), "Some comment text", "another-author", LocalDateTime.now(), UUID.randomUUID());
        CommentCreateRequestFromDto updateRequest = new CommentCreateRequestFromDto("Updated text", comment2.getNewsId());
        when(repository.findById(comment2.getId())).thenReturn(Optional.of(comment2));
        when(newsServicePort.getNewsById(comment2.getNewsId())).thenReturn(null);
        //when
        //then
        assertThrows(AccessDeniedException.class, () -> commentService.updateComment(comment2.getId(), updateRequest));
    }

    @Test
    void shouldDeleteComment() throws AccessDeniedException {
        // given
        UUID id = comment.getId();
        when(repository.findById(id)).thenReturn(Optional.of(comment));
        doNothing().when(repository).deleteById(id);
        //when
        commentService.deleteComment(id);
        //then
        verify(repository, times(1)).deleteById(id);
        verify(cache, times(1)).remove(id);
    }

    @Test
    void shouldDeleteCommentNotFound() {
        // given
        UUID id = comment.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundException.class, () -> commentService.deleteComment(id));
    }
}

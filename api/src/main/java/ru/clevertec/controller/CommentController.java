package ru.clevertec.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.api.ApiResponse;
import ru.clevertec.dto.CommentCreateRequest;
import ru.clevertec.dto.CommentDto;
import ru.clevertec.service.CommentService;

import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public Page<CommentDto> getAllComments(Pageable pageable) {
        return commentService.getAllComments(pageable);
    }

    @GetMapping("/{newsId}")
    public Page<CommentDto> getCommentsByNewsId(@PathVariable UUID newsId, Pageable pageable) {
        return commentService.getCommentsByNewsId(newsId, pageable);
    }

    @PostMapping
    public ApiResponse<CommentDto> createComment(@RequestBody CommentCreateRequest comment) {
        CommentDto createdComment = commentService.createComment(comment);
        return ApiResponse.<CommentDto>builder()
                .message("Комментарий успешно создан")
                .status(true)
                .data(createdComment)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteComment(@PathVariable UUID id) {
        commentService.deleteComment(id);
        return ApiResponse.<Void>builder()
                .message("Комментарий успешно удален")
                .status(true)
                .build();
    }
}

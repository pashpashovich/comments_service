package ru.clevertec.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.api.ApiResponse;
import ru.clevertec.dto.CommentCreateRequest;
import ru.clevertec.dto.CommentDto;
import ru.clevertec.service.CommentService;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CommentDto>>> getAllComments(Pageable pageable) {
        Page<CommentDto> comments = commentService.getAllComments(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<CommentDto>>builder()
                .data(comments)
                .status(true)
                .message("Все комментарии получены")
                .build());
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<ApiResponse<Page<CommentDto>>> getCommentsByNewsId(@PathVariable UUID newsId, Pageable pageable) {
        Page<CommentDto> comments = commentService.getCommentsByNewsId(newsId, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<CommentDto>>builder()
                .data(comments)
                .status(true)
                .message("Получены комментарии к новости")
                .build());
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<ApiResponse<CommentDto>> getCommentById(@PathVariable UUID id) {
        CommentDto comment = commentService.getCommentById(id);
        return ResponseEntity.ok(ApiResponse.<CommentDto>builder()
                .data(comment)
                .status(true)
                .message("Получен комментарий")
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentDto>> createComment(@Valid @RequestBody CommentCreateRequest comment) {
        CommentDto createdComment = commentService.createComment(comment);
        return ResponseEntity.ok(ApiResponse.<CommentDto>builder()
                .message("Комментарий успешно создан")
                .status(true)
                .data(createdComment)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDto>> updateComment(@PathVariable UUID id, @RequestBody CommentCreateRequest updateRequest) throws AccessDeniedException {
        CommentDto updatedComment = commentService.updateComment(id, updateRequest);
        return ResponseEntity.ok(ApiResponse.<CommentDto>builder()
                .data(updatedComment)
                .status(true)
                .message("Комментарий обновлен")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable UUID id) throws AccessDeniedException {
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Комментарий успешно удален")
                .status(true)
                .build());
    }
}

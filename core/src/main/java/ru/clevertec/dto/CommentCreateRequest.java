package ru.clevertec.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CommentCreateRequest {
    private String text;
    private String username;
    private UUID newsId;
}

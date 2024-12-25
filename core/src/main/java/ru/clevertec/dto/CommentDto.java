package ru.clevertec.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {
    private UUID id;
    private String text;
    private String username;
    private LocalDateTime createdAt;
}

package ru.clevertec.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Comment {
    private UUID id;
    private String text;
    private String username;
    private LocalDateTime createdAt;
    private UUID newsId;
}

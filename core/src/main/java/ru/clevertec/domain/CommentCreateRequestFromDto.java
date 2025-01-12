package ru.clevertec.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CommentCreateRequestFromDto {
    private String text;
    private UUID newsId;
}

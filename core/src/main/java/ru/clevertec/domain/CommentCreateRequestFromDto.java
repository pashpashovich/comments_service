package ru.clevertec.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CommentCreateRequestFromDto {
    private String text;
    private UUID newsId;
}

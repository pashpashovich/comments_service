package ru.clevertec.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NewsFromDto {
    private String title;
    private String text;
    private LocalDateTime createdAt;
}

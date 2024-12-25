package ru.clevertec.port;

import ru.clevertec.dto.NewsDto;

import java.util.UUID;

public interface NewsServicePort {
    NewsDto getNewsById(UUID id);
}

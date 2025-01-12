package ru.clevertec.port;

import ru.clevertec.domain.NewsFromDto;

import java.util.UUID;

public interface NewsServicePort {
     NewsFromDto getNewsById(UUID id);
}

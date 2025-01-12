package ru.clevertec.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.client.NewsClient;
import ru.clevertec.domain.NewsFromDto;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.mapper.CommentsDomainMapper;
import ru.clevertec.port.NewsServicePort;

import java.util.UUID;

@Component
@AllArgsConstructor
public class NewsServiceAdapter implements NewsServicePort {

    private final NewsClient newsClient;
    private final CommentsDomainMapper commentsDomainMapper;

    @Override
    public NewsFromDto getNewsById(UUID id) {
        if (newsClient.getNewsById(id).getBody().getData() == null)
            throw new NotFoundException(String.format("Новость с id %s не найдена", id));
        return commentsDomainMapper.toNewsFromDto(newsClient.getNewsById(id).getBody().getData());
    }
}

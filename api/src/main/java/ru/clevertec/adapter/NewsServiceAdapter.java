package ru.clevertec.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.client.NewsClient;
import ru.clevertec.dto.NewsDto;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.port.NewsServicePort;

import java.util.UUID;

@Component
@AllArgsConstructor
public class NewsServiceAdapter implements NewsServicePort {

    private final NewsClient newsClient;

    @Override
    public NewsDto getNewsById(UUID id) {
        if (newsClient.getNewsById(id).getBody().getData() == null)
            throw new NotFoundException(String.format("Новость с id %s не найдена", id));
        return newsClient.getNewsById(id).getBody().getData();
    }
}

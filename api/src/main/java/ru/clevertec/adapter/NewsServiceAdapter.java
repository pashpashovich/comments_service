package ru.clevertec.adapter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.client.NewsClient;
import ru.clevertec.dto.NewsDto;
import ru.clevertec.port.NewsServicePort;

import java.util.UUID;

@Component
@AllArgsConstructor
public class NewsServiceAdapter implements NewsServicePort {

    private final NewsClient newsClient;

    @Override
    public NewsDto getNewsById(UUID id) {
        return newsClient.getNewsById(id);
    }
}

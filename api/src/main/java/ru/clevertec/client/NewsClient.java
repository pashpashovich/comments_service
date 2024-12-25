package ru.clevertec.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.dto.NewsDto;

import java.util.UUID;

@FeignClient(name = "news-service", url = "http://localhost:8081")
public interface NewsClient {
    @GetMapping("/news/{id}")
    NewsDto getNewsById(@PathVariable UUID id);
}

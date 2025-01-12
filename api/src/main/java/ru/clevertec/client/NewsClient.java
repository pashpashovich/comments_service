package ru.clevertec.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.api.ApiResponse;
import ru.clevertec.dto.NewsDto;

import java.util.UUID;

@FeignClient(name = "news-service", url = "${feign.news-service.url}")
public interface NewsClient {
    @GetMapping("/news/{id}")
    ResponseEntity<ApiResponse<NewsDto>>getNewsById(@PathVariable UUID id);
}

package ru.clevertec.exception_handler;

import feign.FeignException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.api.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    public ApiResponse<String> handleNotFound(FeignException.NotFound ex) {
        return ApiResponse.<String>builder()
                .data("Новость с таким ID не найдена")
                .status(false)
                .build();
    }
}

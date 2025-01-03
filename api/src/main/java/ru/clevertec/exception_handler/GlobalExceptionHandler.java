package ru.clevertec.exception_handler;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.api.ApiResponse;
import ru.clevertec.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handlePermissionException(AccessDeniedException ex) {
        ApiResponse<ErrorResponse> apiResponse = ApiResponse.<ErrorResponse>builder()
                .message(ex.getMessage())
                .status(false)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationFailed(MethodArgumentNotValidException e) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .data("Валидация не прошла: " + e)
                .status(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .data("Новость с таким ID не найдена")
                .status(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleFeignException(FeignException ex) {
        ApiResponse<ErrorResponse> apiResponse = ApiResponse.<ErrorResponse>builder()
                .message(ex.getMessage())
                .status(false)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
}

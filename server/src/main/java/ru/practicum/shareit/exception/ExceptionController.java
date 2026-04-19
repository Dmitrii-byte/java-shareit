package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn("Ресурс не найден: {}", e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedDataException(final DuplicatedDataException e) {
        log.warn("Дублирующиеся данные: {}", e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler(ConditionsNotMetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConditionsNotMetException(final ConditionsNotMetException e) {
        log.warn("Условия не выполнены: {}", e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final ForbiddenException e) {
        log.warn("Доступ запрещён: {}", e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllUncaughtException(final Exception e) {
        log.error("Внутренняя ошибка сервера: {}", e.getMessage(), e);
        return new ErrorResponse("Внутренняя ошибка сервера", "Произошла непредвиденная ошибка");
    }
}
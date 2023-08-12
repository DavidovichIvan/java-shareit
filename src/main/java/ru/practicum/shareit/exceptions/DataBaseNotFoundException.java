package ru.practicum.shareit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataBaseNotFoundException extends RuntimeException {
    private final String message;

    public DataBaseNotFoundException() {
        this.message = "Пользователь/вещь не найдены.";
    }

    public DataBaseNotFoundException(String message) {
        this.message = message;
    }
}
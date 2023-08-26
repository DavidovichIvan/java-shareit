package ru.practicum.shareit.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String message;

    public NotFoundException() {
        this.message = "Пользователь/вещь не найдены.";
    }

    public NotFoundException(String message) {
        this.message = message;
    }
}
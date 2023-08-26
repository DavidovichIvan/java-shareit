package ru.practicum.shareit.exceptions;

import lombok.Getter;

@Getter
public class ServerErrorException extends RuntimeException {

    private final String message;

    public ServerErrorException() {
        this.message = "Ошибка сервера.";
    }

    public ServerErrorException(String message) {
        this.message = message;
    }
}
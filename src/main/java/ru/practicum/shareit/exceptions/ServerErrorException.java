package ru.practicum.shareit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerErrorException extends RuntimeException {

    private final String message;

    public ServerErrorException() {
        this.message = "Ошибка сервера.";
    }

    public ServerErrorException(String message) {
        this.message = message;
    }

}
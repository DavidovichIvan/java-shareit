
package ru.practicum.shareit.exceptions;

import lombok.Getter;

@Getter
public class DataBaseException extends RuntimeException {

    private final String message;

    public DataBaseException() {
        this.message = "Ошибка работы с хранилищем данных!";
    }

    public DataBaseException(String message) {
        this.message = message;
    }
}
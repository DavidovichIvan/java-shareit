
package ru.practicum.shareit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
//@ResponseBody
public class DataBaseException extends RuntimeException {

    private final String message;
    private final String error = "Some error";

    public DataBaseException() {
        this.message = "Ошибка работы с хранилищем данных.";
    }

    public DataBaseException(String message) {
        this.message = message;

    }
}


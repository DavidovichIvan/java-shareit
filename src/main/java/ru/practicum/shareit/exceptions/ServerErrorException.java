package ru.practicum.shareit.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

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

    @ResponseBody
    Map<String, String> showCustomMessage(Exception e) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Unknown state: UNSUPPORTED_STATUS");

        return response;
    }


}
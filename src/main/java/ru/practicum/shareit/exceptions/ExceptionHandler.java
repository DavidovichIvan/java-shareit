package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    Map<String, String> showCustomMessage(Exception e) {

        Map<String, String> response = new HashMap<>();
        response.put("error", "Unknown state: UNSUPPORTED_STATUS");

        return response;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataBaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse notFoundHandle(final DataBaseNotFoundException e) {
        return new ErrorResponse(
                "Ошибка данных", e.getMessage()
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataBaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse validationHandle(final DataBaseException e) {
        return new ErrorResponse(
                "Ошибка данных", e.getMessage()
        );
    }
}
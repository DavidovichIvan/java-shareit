package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.Headers;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dtoAndValidation.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                    @RequestBody @Valid RequestDto requestDto) {
        log.info("Запрос на создание реквест вещи");
        return requestClient.createRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(@RequestHeader(Headers.REQUESTER_ID) Long userId) {
        log.info("Запрос реквестов полязователя с id {}", userId);
        return requestClient.getRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                   Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20")
                                                       Integer size) {
        log.info("Запрос всех реквестов");
        return requestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                             @PositiveOrZero @PathVariable Long requestId) {
        log.info("Запрос реквеста с id {}", requestId);
        return requestClient.getRequestById(userId, requestId);
    }

}

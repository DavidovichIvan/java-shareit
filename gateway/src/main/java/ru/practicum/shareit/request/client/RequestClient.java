package ru.practicum.shareit.request.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.RestCreator;
import ru.practicum.shareit.request.dtoAndValidation.RequestDto;

import java.util.Map;

@Slf4j
@Component
@Profile("someProfile")
public class RequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(RestCreator restCreator) {
        super(restCreator.createRestTemplate(API_PREFIX));
    }

    public ResponseEntity<Object> createRequest(Long userId, RequestDto requestDto) {
        log.debug("POST Request from User: {}, request: {}", userId, requestDto);
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getRequestsByUser(Long userId) {
        log.debug("GET requests for User id: {}", userId);
        return get("", userId);
    }

    public ResponseEntity<Object> getAllRequests(long userId, Integer from, Integer size) {
        log.debug("GET all requests for User id: {}, from: {}, size: {}", userId, from, size);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
        log.debug("GET Request id: {}, by User id: {}", requestId, userId);
        return get("/" + requestId, userId);
    }

}

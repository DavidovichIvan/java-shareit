package ru.practicum.shareit.user.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.RestCreator;
import ru.practicum.shareit.user.dtoAndValidation.UserDto;
@Slf4j
@Component
@Profile("someProfile")
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(RestCreator restCreator) {
        super(restCreator.createRestTemplate(API_PREFIX));
    }

    public ResponseEntity<Object> createUser(UserDto userDto) {
        log.debug("POST User: {}", userDto);
        return post("", userDto);
    }

    public ResponseEntity<Object> updateUser(UserDto userDto, Long id) {
        log.debug("PATCH User id: {},  updated User: {}", id, userDto);
        return patch("/" + id, userDto);
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        log.debug("DELETE User id: {}", userId);
        return delete("/" + userId);
    }

    public ResponseEntity<Object> getUser(Long userId) {
        log.debug("GET User id: {}", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> getAllUsers() {
        log.debug("GET all Users");
        return get("");
    }
}

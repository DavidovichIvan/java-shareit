package ru.practicum.shareit.item.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.RestCreator;
import ru.practicum.shareit.item.dtoAndValidation.CommentDto;
import ru.practicum.shareit.item.dtoAndValidation.ItemDto;

import java.util.Map;

@Slf4j
@Component
@Profile("someProfile")
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(RestCreator restCreator) {
        super(restCreator.createRestTemplate(API_PREFIX));
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDto itemDto) {
        log.debug("POST Item from User: {}, booking: {}", userId, itemDto);
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemDto itemDto) {
        log.debug("PATCH Item: {} from User: {},  updated Item: {},", itemId, userId, itemDto);
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> getItem(Long itemId, Long userId) {
        log.debug("GET Item id: {} from User id: {}", itemId, userId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemsForOwner(Long userId, Integer from, Integer size) {
        log.debug("GET all items for owner User id: {}, from: {}, size: {}", userId, from, size);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> searchItems(String text, Integer from, Integer size) {
        log.debug("GET Items by search parameter: {}", text);
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentDto commentDto) {
        log.debug("POST Comment to Item id: {} by User id: {}", itemId, userId);
        return post("/" + itemId + "/comment", userId, commentDto);
    }

}

package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.Headers;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dtoAndValidation.CommentDto;
import ru.practicum.shareit.item.dtoAndValidation.ItemDto;
import ru.practicum.shareit.client.ValidationMarker;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Slf4j
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                             @Validated(ValidationMarker.OnCreate.class) @RequestBody ItemDto itemDto) {
        log.info("Запрос на создание вещи");
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(Headers.REQUESTER_ID) Long userId, @PathVariable Long itemId,
                                             @Validated(ValidationMarker.OnUpdate.class) @RequestBody ItemDto itemDto) {
        log.info("Запрос на изменение вещи с id {}", itemId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(Headers.REQUESTER_ID) Long userId, @PathVariable Long itemId) {
        log.info("Запрос вещи с id {}", itemId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsForOwner(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                   Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20")
                                                   Integer size) {
        log.info("Запрос вещей пользователя с id {}", userId);
        return itemClient.getItemsOwners(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String text,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                              Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "20")
                                              Integer size) {
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                                @PathVariable Long itemId,
                                                @Valid @RequestBody CommentDto commentDto) {
        log.info("Запрос на добавление отзыва к вещи с id {}", itemId);
        return itemClient.createComment(userId, itemId, commentDto);
    }
}
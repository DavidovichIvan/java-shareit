package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Item addItem(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody Item item) {
        log.info("Запрос на добавление вещи");
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item updItem(@PathVariable("itemId") int itemId,
                        @RequestHeader("X-Sharer-User-Id") int userId,
                        @RequestBody Item item) {
        log.info("Запрос на обновление вещи");
        return itemService.updItem(itemId, userId, item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable("itemId") int itemId) {
        log.info("Запрос на просмотр вещи");
        return itemService.getItem(itemId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemsOfOneUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Запрос на просмотре всех вещей пользователя");
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItem(@RequestParam(defaultValue = "") String text) {
        log.info("Запрос на поиск вещи. Текст для поиска " + text);
        return itemService.searchItem(text);
    }
}
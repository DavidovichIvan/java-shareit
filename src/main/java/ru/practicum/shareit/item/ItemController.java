package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
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
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public Item updItem(@PathVariable("itemId") int itemId,
                        @RequestHeader("X-Sharer-User-Id") int userId,
                        @RequestBody Item item) {
        return itemService.updItem(itemId, userId, item);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable("itemId") int itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemsOfOneUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItem(@RequestParam(defaultValue = "") String text) {
        return itemService.searchItem(text);
    }
}
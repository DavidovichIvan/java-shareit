package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestBody Item item) {
        return itemService.addItem(ownerId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updItem(@PathVariable("itemId") int itemId,
                        @RequestHeader("X-Sharer-User-Id") int ownerId,
                        @RequestBody Item item) {
        return itemService.updItem(itemId, ownerId, item);
    }

    @GetMapping()
    public List<Item> getUserItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getUserItems(ownerId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@RequestHeader("X-Sharer-User-Id") int requesterId, @PathVariable("itemId") int itemId) {
        return itemService.getItemById(requesterId, itemId);
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam(defaultValue = "") String text) {
        return itemService.searchItem(text, text);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addComment(@PathVariable("itemId") int itemId,
                              @RequestHeader("X-Sharer-User-Id") int userId,
                              @RequestBody Comment requestBody) {
        return itemService.addComment(itemId, userId, requestBody.getText());
    }

}
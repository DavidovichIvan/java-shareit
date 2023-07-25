package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Slf4j
@Getter
@Setter
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;

    private final UserService userService;

    public Item addItem(int userId, Item item) {
        item.setOwnerId(userId);
        return itemStorage.addItem(item);
    }

    public Item updItem(int itemId, int userId, Item item) {
        item.setId(itemId);
        item.setOwnerId(userId);
        return itemStorage.updItem(item);
    }

    public ItemDto getItem(int itemId) {
        return ItemMapper.itemToDto(itemStorage.getItem(itemId));
    }

    public List<ItemDto> getUserItems(int userId) {
        List<ItemDto> items = ItemMapper.itemsListToDto(itemStorage.getUserItems(userId));
        return items;
    }

    public List<ItemDto> searchItem(String text) {
        List<ItemDto> items = ItemMapper.itemsListToDto(itemStorage.searchItem(text));
        return items;
    }
}
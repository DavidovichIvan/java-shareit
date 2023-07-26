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
        log.info("Запрос на добавление вещи");
        item.setOwnerId(userId);
        return itemStorage.addItem(item);
    }

    public Item updItem(int itemId, int userId, Item item) {
        log.info("Запрос на обновление вещи с id: " + itemId);
        item.setId(itemId);
        item.setOwnerId(userId);
        return itemStorage.updItem(item);
    }

    public ItemDto getItem(int itemId) {
        log.info("Запрос на просмотр вещи с id: " + itemId);
        return ItemMapper.itemToDto(itemStorage.getItem(itemId));
    }

    public List<ItemDto> getUserItems(int userId) {
        log.info("Запрос на просмотр всех вещей пользователя c id: " + userId);
        List<ItemDto> items = ItemMapper.itemsListToDto(itemStorage.getUserItems(userId));
        return items;
    }

    public List<ItemDto> searchItem(String text) {
        log.info("Запрос на поиск вещи. Текст для поиска: " + text);
        List<ItemDto> items = ItemMapper.itemsListToDto(itemStorage.searchItem(text));
        return items;
    }
}
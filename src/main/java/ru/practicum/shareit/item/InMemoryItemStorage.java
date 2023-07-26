package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.DataBaseException;
import ru.practicum.shareit.exceptions.DataBaseNotFoundException;
import ru.practicum.shareit.user.InMemoryUserStorage;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {

    private final InMemoryUserStorage userStorage;

    @Getter
    @Setter
    private Map<Integer, Item> itemsList = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        if (itemsList.containsKey(item.getId())) {
            log.info("Вещь с id: {} уже существует", item.getId());
            throw new DataBaseException("Вещь уже существует, id: " + item.getId());
        }

        itemValidate(item);
        itemsList.put(item.getId(), item);

        log.info("Добавлена вещь: {} ", item);
        Item.setItemIdCounter(Item.getItemIdCounter() + 1);

        userStorage.addItemToUser(item);

        return item;
    }

    @Override
    public Item updItem(Item item) {
        userStorage.checkUserHasItem(item);

        Item itemToUpdate = itemsList.get(item.getId());

        if (item.getName() != null) {
            itemToUpdate.setName(item.getName());
        }

        if (item.getDescription() != null) {
            itemToUpdate.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            if (item.getAvailable().equals(true)) {
                itemToUpdate.setAvailable(true);
            }
            if (item.getAvailable().equals(false)) {
                itemToUpdate.setAvailable(false);
            }
        }
        itemsList.put(itemToUpdate.getId(), itemToUpdate);

        return itemToUpdate;
    }

    @Override
    public Item getItem(int itemId) {
        if (!itemsList.containsKey(itemId)) {
            log.info("Вещь не найдена, id вещи: " + itemId);
            throw new DataBaseNotFoundException("Вещь не найдена, id вещи: " + itemId);
        }
        return itemsList.get(itemId);
    }

    @Override
    public List<Item> getUserItems(int userId) {
        if (!userStorage.getUsersList().containsKey(userId)) {
            log.info("Не найден пользователь с id: {} ", userId);
            throw new DataBaseNotFoundException("Не найден пользователь с id: " + userId);
        }
        List<Item> items = new ArrayList<>();
        Set<Integer> userItems = userStorage
                .getUser(userId)
                .getUserItemsToShare();

        for (int i : userItems) {
            items.add(itemsList.get(i));
        }
        return items;
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> items = new ArrayList<>();
        if (text == null || text.isBlank()) {
            return items;
        }
        for (Item i : itemsList.values()) {
            if ((i.getName().toLowerCase()
                    .contains(text.toLowerCase()) || i.getDescription()
                    .toLowerCase().contains(text.toLowerCase())) && i.getAvailable().equals(true)) {
                items.add(i);
            }
        }
        return items;
    }

    private void itemValidate(Item item) {
        if (!userStorage.getUsersList().containsKey(item.getOwnerId())) {
            log.info("Вещь не добавлена. Не найден пользователь с id: {} ", item.getOwnerId());
            throw new DataBaseNotFoundException("Невозможно добавить вещь, не найден пользователь-владелец");
        }

        if (item.getName() == null || item.getName().isBlank()) {
            log.info("Введено пустое название вещи");
            throw new DataBaseException("Название вещи не может быть пустым.");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            log.info("Не введено описание вещи");
            throw new DataBaseException("Не введено описание вещи.");
        }

        if (item.getAvailable() == null) {
            log.info("Не указан статус доступности вещи.");
            throw new DataBaseException("Не указан статус доступности вещи.");
        }
    }


}
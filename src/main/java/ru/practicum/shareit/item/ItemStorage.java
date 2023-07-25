package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {

    Item addItem(Item item);

    Item updItem(Item item);

    Item getItem(int itemId);

    List<Item> getUserItems(int userId);

    List<Item> searchItem(String text);
}
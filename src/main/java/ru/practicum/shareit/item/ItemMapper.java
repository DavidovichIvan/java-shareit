package ru.practicum.shareit.item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto itemToDto(Item item) {
        ItemDto mappedItem = new ItemDto();
        mappedItem.setId(item.getId());
        mappedItem.setName(item.getName());
        mappedItem.setDescription(item.getDescription());
        mappedItem.setAvailable(item.getAvailable());
        return mappedItem;
    }

    public static List<ItemDto> itemsListToDto(List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList<>();

        for (Item i : items) {
            itemsDto.add(itemToDto(i));

        }
        return itemsDto;
    }


}
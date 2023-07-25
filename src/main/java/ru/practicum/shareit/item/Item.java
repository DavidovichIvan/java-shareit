package ru.practicum.shareit.item;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Item {
    @Getter
    @Setter
    private static int itemIdCounter = 1;

    private int id;
    private int ownerId;
    private String name;
    private String description;

    private Boolean available;


    public Item() {
        this.id = itemIdCounter;
    }

    public Item(String name, String description) {
        this.name = name;
        this.description = description;

        this.id = itemIdCounter;
    }
}
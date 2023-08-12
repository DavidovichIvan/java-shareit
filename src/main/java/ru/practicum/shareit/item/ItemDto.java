package ru.practicum.shareit.item;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
}
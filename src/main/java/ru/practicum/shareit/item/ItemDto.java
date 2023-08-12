package ru.practicum.shareit.item;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
//@AllArgsConstructor
//@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
}
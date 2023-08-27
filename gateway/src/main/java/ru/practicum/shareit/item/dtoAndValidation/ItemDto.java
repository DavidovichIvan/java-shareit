package ru.practicum.shareit.item.dtoAndValidation;

import lombok.*;
import ru.practicum.shareit.client.ValidationMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(groups = {ValidationMarker.OnCreate.class})
    private String name;
    @NotBlank(groups = {ValidationMarker.OnCreate.class})
    private String description;
    @NotNull(groups = {ValidationMarker.OnCreate.class})
    private Boolean available;
    private Long requestId;
}

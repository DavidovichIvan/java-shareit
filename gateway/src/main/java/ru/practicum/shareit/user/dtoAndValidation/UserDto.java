package ru.practicum.shareit.user.dtoAndValidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.client.ValidationMarker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = {ValidationMarker.OnCreate.class})
    @Email(message = "Некорректный формат электронной почты: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            groups = {ValidationMarker.OnCreate.class, ValidationMarker.OnUpdate.class})
    private String email;
    @NotBlank(groups = {ValidationMarker.OnCreate.class})
    private String name;
}
package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void checkUserExists(int userId) {
        User u = userRepository
                .findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));
    }

    public void prepareUserToUpdate(int userId, User updUser) {
        User oldUser = userRepository
                .findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));

        if (updUser.getName() == null || updUser.getName().isBlank()) {
            updUser.setName(oldUser.getName());
        }

        if (updUser.getEmail() == null || updUser.getEmail().isBlank()) {
            updUser.setEmail(oldUser.getEmail());
        }
        updUser.setId(userId);
    }

}
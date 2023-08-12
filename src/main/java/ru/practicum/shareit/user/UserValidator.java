package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.DataBaseException;
import ru.practicum.shareit.exceptions.DataBaseNotFoundException;
import ru.practicum.shareit.item.ItemRepository;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void userValidate(User user) {
        if (user.getName().isBlank() || user.getEmail() == null || user.getEmail().isEmpty()) {
            log.info("Введено пустое имя пользователя/адрес электронной почты");
            throw new DataBaseException("Имя пользователя не может быть пустым.");
        }
        if (!emailValidate(user.getEmail())) {
            log.info("Адрес электронной почты не введен или введен в неверном формате {} ", user.getEmail());
            throw new DataBaseException("Электронная почта введена в неверном формате.");
        }
    }

    public void checkUserExists(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new DataBaseNotFoundException("Не найден пользователь с id: " + userId);
        }
    }

    public void prepareUserToUpdate(int userId, User updUser) {
        Optional<User> oldUser = userRepository.findById(userId);

        if (oldUser.isEmpty()) {
            throw new DataBaseNotFoundException("Не найден пользователь с id: " + userId);
        }

        if (updUser.getName() == null || updUser.getName().isBlank()) {
            updUser.setName(oldUser.get().getName());
        }

        if (updUser.getEmail() == null || updUser.getEmail().isBlank()) {
            updUser.setEmail(oldUser.get().getEmail());
        }
        updUser.setId(userId);
    }


    private static boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
}
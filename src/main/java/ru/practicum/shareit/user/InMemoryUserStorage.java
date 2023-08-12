package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.DataBaseException;
import ru.practicum.shareit.exceptions.DataBaseNotFoundException;
import ru.practicum.shareit.exceptions.ServerErrorException;
import ru.practicum.shareit.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Setter
@Component
public class InMemoryUserStorage implements UserStorage {
    static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Getter
    Map<Integer, User> usersList = new HashMap<>();

    @Override
    public User addUser(User user) {
        if (usersList.containsKey(user.getId())) {
            log.info("Пользователь с id: {} уже существует", user.getId());
            throw new DataBaseException("Пользователь с таким id уже существует, id: " + user.getId());
        }

        userValidate(user);
        usersList.put(user.getId(), user);

        log.info("Добавлен пользователь пользователь: {} ", user);
        User.setUserIdCounter(User.getUserIdCounter() + 1);
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkUserExists(user.getId());
        checkOtherUserWithSameEmailExists(user);

        User updatedUser = usersList.get(user.getId());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }

        usersList.put(user.getId(), updatedUser);
        log.info("Запрос на обновление; обновлен пользователь: {} ", user);
        return updatedUser;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", usersList.size());
        List<User> users = new ArrayList<>(usersList.values());
        return users;
    }

    @Override
    public User getUser(int id) {
        checkUserExists(id);
        return usersList.get(id);
    }

    @Override
    public void deleteUser(int id) {
        checkUserExists(id);
        usersList.remove(id);
    }

    private void userValidate(User user) {
        if (user.getName().isBlank() || user.getEmail() == null || user.getEmail().isEmpty()) {
            log.info("Введено пустое имя пользователя/адрес электронной почты");
            throw new DataBaseException("Имя пользователя не может быть пустым.");
        }

        if (!emailValidate(user.getEmail())) {
            log.info("Адрес электронной почты не введен или введен в неверном формате {} ", user.getEmail());
            throw new DataBaseException("Электронная почта введена в неверном формате.");
        }

        for (User u : usersList.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.info("Пользователь с таким адресом уже зарегистрирован. {} ", user.getEmail());
                throw new ServerErrorException("Пользователь с таким адресом уже зарегистрирован.");
            }
        }
    }

    private static boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    private void checkUserExists(int userId) {
        if (!usersList.containsKey(userId)) {
            log.info("Не существует пользователя с id: {} ", userId);
            throw new DataBaseNotFoundException("Пользователь c  таким id не существует, id: " + userId);
        }
    }

    private void checkOtherUserWithSameEmailExists(User user) {
        int userId = user.getId();
        String userEmail = user.getEmail();

        for (User u : usersList.values()) {
            if (u.getId() != userId && u.getEmail().equals(userEmail)) {
                log.info("Невозможно обновить email, пользователь с таким адресом уже зарегистрирован. {} ", user.getEmail());
                throw new ServerErrorException("Пользователь с таким адресом уже зарегистрирован.");
            }
        }
    }

    @Override
    public void addItemToUser(Item item) {
        usersList.get(item.getOwnerId())
                .getUserItemsToShare()
                .add(item.getId());
    }

    @Override
    public void checkUserHasItem(Item item) {
        if (!usersList.get(item.getOwnerId())
                .getUserItemsToShare()
                .contains(item.getId())) {

            log.info("Вещь не найдена у пользователя с id: {} ", item.getOwnerId());
            throw new DataBaseNotFoundException("Вещь не найдена у пользователя c id: " + item.getOwnerId());
        }

    }
}
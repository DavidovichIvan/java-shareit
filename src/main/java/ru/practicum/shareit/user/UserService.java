package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Getter
@Setter
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        log.info("Запрос на добавление пользователя");
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Запрос на обновление пользователя id: {} ", user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        log.info("Запрос списка всех пользователей");
        return userStorage.getAllUsers();
    }

    public User getUser(int id) {
        log.info("Запрос на получение пользователя id: {} ", id);
        return userStorage.getUser(id);
    }

    public void deleteUser(int id) {
        log.info("Запрос на удаление пользователя id: {} ", id);
        userStorage.deleteUser(id);
    }

}
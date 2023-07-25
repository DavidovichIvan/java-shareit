package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUser(int id);

    void deleteUser(int id);
}
package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Getter
@Setter
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserValidator userValidator;

    @Transactional
    public User addUser(User user) {
        log.info("Запрос на добавление пользователя");
        userValidator.userValidate(user);
        return repository.save(user);
    }

    @Transactional
    public User updateUser(int userId, User updUser) {
        log.info("Запрос на обновление пользователя с id: " + userId);
        userValidator.prepareUserToUpdate(userId, updUser);

        return repository.save(updUser);
    }

    public User getUserById(int userId) {
        log.info("Запрос на просмотр пользователя с id: " + userId);
        userValidator.checkUserExists(userId);

        return repository.findById(userId).get();
    }

    public void deleteById(Integer id) {
        log.info("Запрос на удаление пользователя с id: " + id);
        repository.deleteById(id);
    }

    public List<User> getAll() {
        return repository.findAll();
    }
}
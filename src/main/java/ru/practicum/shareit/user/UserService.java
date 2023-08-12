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
//@Transactional(readOnly = true)
@Transactional
@RequiredArgsConstructor
public class UserService {
    //static final Pattern VALID_EMAIL_ADDRESS_REGEX =
    //      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final UserRepository repository;
    private final UserValidator userValidator;

    @Transactional
    public User addUser(User user) {
        log.info("Запрос на добавление пользователя");
        userValidator.userValidate(user);
        /*
        if (user.getName().isBlank() || user.getEmail() == null || user.getEmail().isEmpty()) {
            log.info("Введено пустое имя пользователя/адрес электронной почты");
            throw new DataBaseException("Имя пользователя не может быть пустым.");
        }

        if (!emailValidate(user.getEmail())) {
            log.info("Адрес электронной почты не введен или введен в неверном формате {} ", user.getEmail());
            throw new DataBaseException("Электронная почта введена в неверном формате.");
        }
       */
        return repository.save(user);
    }

    @Transactional
    public User updateUser(int userId, User updUser) {
        log.info("Запрос на обновление пользователя с id: " + userId);
        userValidator.prepareUserToUpdate(userId, updUser);
        /*
        Optional<User> oldUser = repository.findById(userId);

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
        */
        return repository.save(updUser);
    }

    public User getUserById(int userId) {
        log.info("Запрос на просмотр пользователя с id: " + userId);
        userValidator.checkUserExists(userId);
       /*
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new DataBaseNotFoundException("Не найден пользователь с id: " + userId);
        }
        */
        return repository.findById(userId).get();
    }

    public void deleteById(Integer id) {
        log.info("Запрос на удаление пользователя с id: " + id);
        repository.deleteById(id);
    }

    public List<User> getAll() {
        return repository.findAll();
    }
/*
    private static boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
*/

}

/*
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
 */
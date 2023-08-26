package ru.practicum.shareit.Serviсes;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Data
public class UserServiceTest {

    private final UserService userService;

    private List<User> testListUsers = new ArrayList<>();

    User testUser = new User("Batman", "Bat@man.bb");
    User testUser2 = new User("Mike", "mmm@mail.mv");
    User testUser3 = new User("Bob", "bob@mail.mv");
    User testUser4 = new User("Anna", "anna@mail.mv");

    private int numberOfUsersInTestDB;

    @BeforeEach
    public void addDataToDB() {
        userService.addUser(testUser2);
        userService.addUser(testUser3);
        userService.addUser(testUser4);

        numberOfUsersInTestDB = userService.getAll().size();
    }

    @AfterEach
    public void cleanUserList() {
        testListUsers.clear();
    }

    @Test
    public void addAUser() {
        testListUsers = userService.getAll();
        Assertions.assertThat(testListUsers.size() == numberOfUsersInTestDB).isTrue();
        userService.addUser(testUser);

        testListUsers = userService.getAll();
        Assertions.assertThat(testListUsers.size() == numberOfUsersInTestDB + 1).isTrue();

        User userFromDB = userService.getUserById((testUser.getId()));
        Assertions.assertThat(userFromDB.getName().equals("Batman")).isTrue();
    }

    @Test
    public void deleteUser() {
        userService.addUser(testUser);
        testListUsers = userService.getAll();
        Assertions.assertThat(testListUsers.size() == numberOfUsersInTestDB + 1).isTrue();

        userService.deleteById(testUser.getId());

        testListUsers = userService.getAll();
        Assertions.assertThat(testListUsers.size() == numberOfUsersInTestDB).isTrue();
        userService.deleteById(testUser2.getId());
        userService.deleteById(testUser3.getId());
        userService.deleteById(testUser4.getId());
        testListUsers = userService.getAll();
        Assertions.assertThat(testListUsers.size() == numberOfUsersInTestDB - 3).isTrue();
    }

    @Test
    public void getAllUsers() {
        testListUsers = userService.getAll();
        assertThat(userService.getAll().size() == numberOfUsersInTestDB);
        assertThat(testListUsers.contains(testUser2));
        assertThat(testListUsers.contains(testUser3));
        assertThat(testListUsers.contains(testUser4));
    }

    @Test
    public void updateUser() {
        testListUsers = userService.getAll();
        assertThat(testListUsers.contains(testUser2));

        int id = testUser2.getId();
        String name = userService.getUserById(testUser2.getId()).getName();
        String email = userService.getUserById(testUser2.getId()).getEmail();

        String newName = "NewName";
        String newEmail = "NewEmail@new.ne";

        userService.updateUser(id, new User(newName, newEmail));

        User updatedUser = userService.getUserById(id);
        assertThat(updatedUser.getName().equals(newName));
        assertThat(updatedUser.getEmail().equals(newEmail));
    }

}
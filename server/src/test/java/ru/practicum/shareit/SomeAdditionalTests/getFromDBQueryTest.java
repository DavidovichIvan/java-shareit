package ru.practicum.shareit.SomeAdditionalTests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class getFromDBQueryTest {

    private final EntityManager em;

    private final UserService userService;

    private final ItemService itemService;

    private final BookingService bookService;

    private final int ownerId = 1;

    @Test
    void saveUserAndGetBackFromDB() {
        User u = new User("Danny", "Dan@man.da");

        userService.addUser(u);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        User user = query
                .setParameter("email", u.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo("Danny"));
        assertThat(user.getEmail(), equalTo("Dan@man.da"));
    }
/*
    @Test
    void getItemListFromDB() {

        List<Item> listGetByService = itemService.getUserItems(ownerId);
        Assertions.assertThat(!listGetByService.isEmpty()).isTrue();

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.ownerId = :ownerId", Item.class);
        List<Item> listGetByQuery = query.setParameter("ownerId", ownerId).getResultList();

        assertThat(listGetByService.size(), equalToObject(listGetByQuery.size()));
        assertThat(listGetByService.toString(), equalToObject(listGetByQuery.toString()));
        assertThat(listGetByService, equalToObject(listGetByQuery));
    }

 */
}
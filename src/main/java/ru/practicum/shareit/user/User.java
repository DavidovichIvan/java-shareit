package ru.practicum.shareit.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class User {

    @Getter
    @Setter
    private static int userIdCounter = 1;

    private int id;
    private String name;
    private String email;

    private Set<Integer> userItemsToShare;

    public User(String name, String email) {
        this.name = name;
        this.email = email;

        this.userItemsToShare = new HashSet<>();
        this.id = userIdCounter;
    }
}
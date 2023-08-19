package ru.practicum.shareit.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@Proxy(lazy=false)
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "Items", joinColumns = @JoinColumn(name = "owner_id"))
    @Column(name = "item_id")
    private Set<Integer> userItemsToShare = new HashSet<>();

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
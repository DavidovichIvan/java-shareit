package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@Proxy(lazy=false)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int id;

    @Column(name = "item_id", nullable = false)
    private int itemId;

    @Column(name = "user_id", nullable = false)
    private int authorId;

    @Formula("(SELECT u.NAME FROM USERS u\n" +
            "WHERE u.USER_ID = user_id)")
    private String authorName;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "created")
    private LocalDateTime created;

}
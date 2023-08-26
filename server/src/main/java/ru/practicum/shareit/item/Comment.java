package ru.practicum.shareit.item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @EqualsAndHashCode.Include
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
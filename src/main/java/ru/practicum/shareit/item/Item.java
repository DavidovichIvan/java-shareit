package ru.practicum.shareit.item;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
public class Item {
    @Getter     //убрать в конце
    @Setter
    private static int itemIdCounter = 1;
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "owner_id", nullable = false)
    private int ownerId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "available")
    private Boolean available;

    @Transient
    BookingDto lastBooking;
    @Transient
    BookingDto nextBooking;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    List<Comment> comments = new ArrayList<>();

    public Item(String name, String description, int ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId; //хз по идее надо, добавил

        this.id = itemIdCounter;
    }
}
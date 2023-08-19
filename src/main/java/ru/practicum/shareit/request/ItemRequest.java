package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private int id;

    @Column(name = "requester_id", nullable = false)
    private int requesterId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created")
    private LocalDateTime created;

    @Transient
    List<RequestAnswer> items;

    public ItemRequest(String description) {
        this.description = description;
    }
}

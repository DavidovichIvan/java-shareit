package ru.practicum.shareit.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "requester_id", nullable = false)
    private int requesterId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created")
    private LocalDateTime created;

    @Transient
    private List<RequestAnswer> items;

    public ItemRequest(String description) {
        this.description = description;
    }
}
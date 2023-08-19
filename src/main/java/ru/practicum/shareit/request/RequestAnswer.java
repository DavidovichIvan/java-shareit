package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@Transactional(readOnly = true)
public class RequestAnswer {
    @Id
    @Column(name = "item_id")
    public int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "request_id")
    private int requestId;
}
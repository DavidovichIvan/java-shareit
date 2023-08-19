package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestDto {
    @Id
    @Column(name = "request_id")
    private int id;

    @Column(name = "requester_id")
    private int requesterId;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private LocalDateTime created;

    @Transient
    List<RequestAnswer> items;
    //suggestedItemsList хотел по своему назвать чтоб понятней было, но постман не согласился

    public ItemRequestDto(String description) {
        this.description = description;
    }
}
package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class BookingDto {

    public int id;

    private Integer bookerId;

    private LocalDateTime start;

    private LocalDateTime end;

    public BookingDto(int id, Integer bookerId, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.bookerId = bookerId;
        this.start = start;
        this.end = end;
    }
}
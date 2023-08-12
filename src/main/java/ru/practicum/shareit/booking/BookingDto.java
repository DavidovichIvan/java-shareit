package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@RequiredArgsConstructor
public class BookingDto {

    public int id;

    private Integer bookerId;

    private LocalDateTime start;

    private LocalDateTime end;

}
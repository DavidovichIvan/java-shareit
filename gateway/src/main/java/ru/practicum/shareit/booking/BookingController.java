package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dtoAndValidation.BookingDto;
import ru.practicum.shareit.client.Headers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader(Headers.REQUESTER_ID) Long bookerId,
                                                @RequestBody @Valid BookingDto bookingInputDto) {
        log.info("Запрос на новый букинг");
        return bookingClient.createBooking(bookerId, bookingInputDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approvedBooking(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam boolean approved) {
        log.info("Запрос на изменение статуса букинга с id {}");
        return bookingClient.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                                 @PathVariable @NotNull Long bookingId) {
        log.info("Запрос букинга с id {}", bookingId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingByUser(@RequestHeader(Headers.REQUESTER_ID) Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                   Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "20")
                                                   Integer size) {
        log.info("Запрос букингов пользователя с id {}", userId);
        return bookingClient.getBookingByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader(Headers.REQUESTER_ID) Long ownerId,
                                                    @RequestParam(defaultValue = "ALL") String state,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                    Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "20")
                                                    Integer size) {
        return bookingClient.getBookingByOwner(ownerId, state, from, size);
    }

}

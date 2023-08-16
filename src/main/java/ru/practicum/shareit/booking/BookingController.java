package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking add(@RequestHeader("X-Sharer-User-Id") int bookerId, @RequestBody Booking booking) {
        return bookingService.addBooking(bookerId, booking);
    }

    @PatchMapping("/{bookingId}")
    public Booking confirmBooking(@PathVariable("bookingId") int bookingId,
                                  @RequestHeader("X-Sharer-User-Id") int ownerId,
                                  @RequestParam(defaultValue = "") String approved) {
        return bookingService.confirmBooking(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable("bookingId") int bookingId,
                              @RequestHeader("X-Sharer-User-Id") int bookRequestorId) {
        return bookingService.getBookingResult(bookingId, bookRequestorId);
    }

    @GetMapping()
    public List<Booking> getAllBookingsForBooker(@RequestHeader("X-Sharer-User-Id") int bookerId,
                                                 @RequestParam(defaultValue = "ALL") String state) {

        return bookingService.getAllBookingsForBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        log.info("X-Sharer-User-Id=  " + ownerId);
        log.info("state=  " + state);

        return bookingService.getAllBookingsForOwner(ownerId, state);
    }
}
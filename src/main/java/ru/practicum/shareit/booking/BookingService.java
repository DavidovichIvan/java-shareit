package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Getter
@Setter
@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingValidator bookingValidator;

    @Transactional
    public Booking addBooking(int bookerId, Booking booking) {
        log.info("Запрос на аренду");
        bookingValidator.bookValidate(bookerId, booking);

        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(Integer bookingId, Integer ownerId, String status) {
        bookingValidator.statusUpdateValidate(bookingId, ownerId, status);

        status = bookingValidator.statusProcessing(status);

        bookingRepository.updateStatusById(status, bookingId);

        Booking book = bookingRepository.getById(bookingId);
        book.setStatus(status);
        return book;
    }

    public Booking getBookingResult(int bookingId, int bookRequestorId) {
        bookingValidator.bookingViewValidate(bookingId, bookRequestorId);
        return bookingRepository.getById(bookingId);
    }

    public List<Booking> getAllBookingsForBooker(int bookerId, String bookingsState) {
        return bookingValidator.bookingsSearchValidate(bookerId, bookingsState);
    }

    public List<Booking> getAllBookingsForOwner(int ownerId, String bookingsState) {
        return bookingValidator.bookingsForOwnerValidate(ownerId, bookingsState);
    }
}
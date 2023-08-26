package ru.practicum.shareit.Serviсes;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Data
public class BookingServiceTest {

    private final BookingService bookService;

    private final int bookerId = 2;
    private final int bookId = 99;
    private final int itemId = 3;

    private final int ownerId = 1;
    Booking book = new Booking();

    @BeforeEach
    public void beforeEach() {
    }

    @AfterEach
    public void clean() {
    }

    @Test
    public void addBookingAndConfirmBookingAndGetBookingResult() {
        book.setItemId(itemId);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        book.setStart(start);
        book.setEnd(end);
        bookService.addBooking(bookerId, book);

        Booking bookBack = bookService.getBookingResult(book.getId(), bookerId);

        assertThat(bookBack.getStart() == start);
        assertThat(bookBack.getEnd() == end);
        assertThat(bookBack.getItemId() == getItemId());
        assertThat(book.getStatus().equalsIgnoreCase("WAITING"));

        bookService.confirmBooking(book.getId(), ownerId, "true");
        assertThat(bookService.getBookingResult(book.getId(), bookerId)
                .getStatus().equalsIgnoreCase("APPROVED"));
    }

    @Test
    public void getAllBookingsForBooker() {
        String status = "ALL";
        int from = 0;
        int size = 20;
        List<Booking> bookingList = bookService.getAllBookingsForBooker(bookerId, status, from, size);

        assertThat(!bookingList.isEmpty());

        for (Booking b : bookingList) {
            assertThat(b.getItemId() > 0);
            assertThat(b.getBookerId() > 0);
            assertThat(b.getItem() != null);
        }
    }
}
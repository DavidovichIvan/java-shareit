package ru.practicum.shareit.SomeAdditionalTests.Json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;
    private final int id = 1;
    private final int bookerId = 1;
    LocalDateTime start = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS);
    LocalDateTime end = LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.SECONDS);

    @Test
    void testUserDto() throws Exception {
        BookingDto bookingDto = new BookingDto(id,
                bookerId,
                start,
                end);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(id);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(bookerId);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
    }
}

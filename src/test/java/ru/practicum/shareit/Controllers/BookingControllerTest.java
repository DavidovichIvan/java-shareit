package ru.practicum.shareit.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.Item;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final int ownerId = 99;
    private final int bookerId = 111;
    private int bookId = 1;

    private Booking bookMock = new Booking();

    @Test
    void add() throws Exception {
        bookMock.setItem(new Item("Вещь", "Вещь для аренды", ownerId));

        when(bookingService.addBooking(anyInt(), any()))
                .thenReturn(bookMock);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookMock))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is("Вещь")))
                .andExpect(jsonPath("$.item.description", is("Вещь для аренды")));
    }

    @Test
    void confirmBooking() throws Exception {
        bookMock.setId(bookId);
        bookMock.setStatus("APPROVED");

        when(bookingService.confirmBooking(1, 1, "true")).thenReturn(bookMock);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBookingResult(anyInt(), anyInt()))
                .thenReturn(bookMock);
        bookMock.setItem(new Item("Забронированная вещь", "Очень нужна", ownerId));
        bookMock.setBookerId(bookerId);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", bookMock.getBookerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", is("Забронированная вещь")));
    }

    @Test
    void getAllBookingsForBooker() throws Exception {
        when(bookingService.getAllBookingsForBooker(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(new Booking(), new Booking(), new Booking()));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", ownerId)
                        .param("size", "0")
                        .param("from", "4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }

    @Test
    void getBookingNotFoundException() throws Exception {
        when(bookingService.getBookingResult(anyInt(), anyInt()))
                .thenThrow(NotFoundException.class);
        bookMock.setBookerId(bookerId);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", bookMock.getBookerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }
}
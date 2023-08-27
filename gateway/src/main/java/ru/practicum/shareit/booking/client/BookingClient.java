package ru.practicum.shareit.booking.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dtoAndValidation.BookingDto;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.client.RestCreator;

import java.util.Map;
@Slf4j
@Component
//@Profile("default")
// - выставлено по умолчанию, но сейчас не запустится
//т.к. я активировал другой профиль в app.prop
@Profile("someProfile")
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(RestCreator restCreator) {
        super(restCreator.createRestTemplate(API_PREFIX));
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingDto bookingInputDto) {
        log.debug("POST Booking from User: {}, booking: {}", userId, bookingInputDto);
        return post("", userId, bookingInputDto);
    }

    public ResponseEntity<Object> approvedBooking(Long userId, Long bookingId, Boolean approved) {
        log.debug("PATCH Booking from User: {},  set status to {} for bookingId: {},", userId, approved, bookingId);
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        log.debug("GET Booking id: {} from User id: {}", bookingId, userId);
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingByUser(Long userId, String state, Integer from, Integer size) {
        log.debug("GET Booking with state {}, userId: {}, from: {}, size: {}", state, userId, from, size);
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingByOwner(Long userId, String state, Integer from, Integer size) {
        log.debug("GET Booking with state {}, ownerId: {}, from: {}, size: {}", state, userId, from, size);
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

}

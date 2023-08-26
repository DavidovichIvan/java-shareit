package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dtoAndValidate.BookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory((serverUrl + API_PREFIX)))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    } //это наследник BaseClienta, у которого есть RestTemplate rest (запрос к серверу формируется в нем)
    // = в конструкторе выше мы по сути прописываем путь (эндпоинт на серевер наш)
    //В итоге у нас здесь уже у BookingClienta появляется неявно поле rest из родительнского класса со строкой запроса
    //в итоге у объекта BookingClient и оставльных таких же где-то должно использоваться поле rest; сам БукингКлиент создается в контроллере но rest я там не вижу


    public ResponseEntity<Object> createBooking(Long userId, BookingDto bookingInputDto) {
        return post("", userId, bookingInputDto); //тут обращаемся к методу родительского класса, который типа универсальный;
        // path это передаем дополнительный путь если есть помимо основного пути контроллера и передаем параметры
        //по идее здесь при вызове родительского метода пост вутри него уже будет обращение к серверу
    }

    public ResponseEntity<Object> approvedBooking(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingByUser(Long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingByOwner(Long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

}

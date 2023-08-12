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

    // private final UserRepository userRepository; //хз возможно не понадобится
    // private final ItemRepository itemRepository;

    @Transactional
    public Booking addBooking(int bookerId, Booking booking) {
        log.info("Запрос на аренду");
        bookingValidator.bookValidate(bookerId, booking);

        return bookingRepository.save(booking);
    }

    //  @Transactional(propagation = Propagation.REQUIRES_NEW) //хз что за значения надо смотреть

    public Booking confirmBooking(Integer bookingId, Integer ownerId, String status) {
        bookingValidator.statusUpdateValidate(bookingId, ownerId, status);

        //  log.info("Статус изначальный= " + status);
        status = bookingValidator.statusProcessing(status);

        //  log.info("Статус приведенный в соответствие для изменения в БД= " + status);
        bookingRepository.updateStatusById(status, bookingId); //здесь сам статус заявки изменяется в БД

        Booking book = bookingRepository.getById(bookingId);
        book.setStatus(status); //хитринка для возвращаемого объекта; в базе поменялось а тут чето троит
        return book;
    }

    public Booking getBookingResult(int bookingId, int bookRequestorId) {
        bookingValidator.bookingViewValidate(bookingId, bookRequestorId);
        return bookingRepository.getById(bookingId);
    }


    public List<Booking> getAllBookingsForBooker(int bookerId, String bookingsState) {
        //то есть нужно все букинги получить, где userId это = букер_id + сортировка

        return bookingValidator.bookingsSearchValidate(bookerId, bookingsState);
    }

    public List<Booking> getAllBookingsForOwner(int ownerId, String bookingsState) {

        return bookingValidator.bookingsForOwnerValidate(ownerId, bookingsState);
    }


}


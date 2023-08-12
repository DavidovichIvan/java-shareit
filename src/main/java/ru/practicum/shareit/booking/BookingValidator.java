package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.DataBaseException;
import ru.practicum.shareit.exceptions.DataBaseNotFoundException;
import ru.practicum.shareit.exceptions.ServerErrorException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemValidator;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserValidator;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingValidator {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemValidator itemValidator;
    private final UserValidator userValidator;

    public static final String[] SET_VALUES = new String[]{"ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "APPROVED", "REJECTED"};
    public static final Set<String> BOOKING_REQUEST_STATUS = new HashSet<>(Arrays.asList(SET_VALUES));


    public void bookValidate(int bookerId, Booking book) {
        if (itemRepository.getByIdAndOwnerId(book.getItemId(), bookerId).isPresent()) {
            throw new DataBaseNotFoundException("Владелец не может забронировать свою вещь.");
        }

        book.setBookerId(bookerId);
        if (book.getItemId() == null ||
                book.getEnd() == null ||
                book.getStart() == null ||
                book.getBookerId() == null) {
            throw new DataBaseException("Введены неполные данные при бронировании вещи.");
        }

        itemValidator.checkItemExists(book.getItemId());
        userValidator.checkUserExists(bookerId);
        if (!itemRepository.findById(book.getItemId()).get().getAvailable()) {
            throw new DataBaseException("В настоящее время вещь недоступна для бронирования.");
        }

        if (book.getStart().isBefore(LocalDateTime.now())) {
            throw new DataBaseException("Дата начала аренды не может быть в прошлом.");
        }
        if (book.getEnd().isBefore(book.getStart())) {
            throw new DataBaseException("Дата окончания аренды не может быть ранее даты начала.");
        }

        if (book.getEnd().equals(book.getStart())) {
            throw new DataBaseException("Дата окончания аренды не может быть равна дате начала.");
        }

        book.setItem(itemRepository.getById(book.getItemId()));
        book.setBooker(userRepository.getById(book.getBookerId()));
    }

    public void statusUpdateValidate(Integer bookingId, Integer ownerId, String status) {
        //букер не может статус изменять
        int bookerId = bookingRepository.findById(bookingId).get().getBookerId();
        if (bookerId == ownerId) {
            throw new DataBaseNotFoundException("Арендатор не может изменять статус заявки.");
        }

        if (bookingRepository.findById(bookingId).get().getStatus().equalsIgnoreCase(String.valueOf(BookingState.APPROVED))) {
            throw new DataBaseException("Заявка  на букинг уже одобрена.");
        }
        userValidator.checkUserExists(ownerId);
        int itemId = bookingRepository.getById(bookingId).getItemId();

        if (itemRepository.getByIdAndOwnerId(itemId, ownerId).isEmpty()) {
            throw new DataBaseException("Пользователь с id: " + ownerId + " не является владельцем вещи с id: " + itemId);
        }

        if (status == null || status.isBlank() || (!status.equals("true") && !status.equals("false"))) {
            throw new DataBaseException("Статус заявки на аренду передан в неверном формате.");
        }
    }

    public String statusProcessing(String status) {
        if (status.equals("true")) {
            return String.valueOf(BookingState.APPROVED);
        }
        if (status.equals("false")) {
            return String.valueOf(BookingState.REJECTED);
        }
        return status;
    }

    public void bookingViewValidate(int bookingId, int requestorId) {
        if (bookingRepository.findById(bookingId).isEmpty()) {
            throw new DataBaseNotFoundException("Не найден букинг с id: " + bookingId);
        }
        int itemId = bookingRepository.getById(bookingId).getItemId();
        if (bookingRepository.getByIdAndBookerId(bookingId, requestorId).isEmpty()
                && itemRepository.getByIdAndOwnerId(itemId, requestorId).isEmpty()) {
            throw new DataBaseNotFoundException("Отсутствуют права на просмотр букинга.");
        }
    }

    public List<Booking> bookingsSearchValidate(int bookerId, String bookingsState) {
        bookingsState = bookingsState.toUpperCase();
        if (!BOOKING_REQUEST_STATUS.contains(bookingsState)) {
            throw new ServerErrorException(bookingsState);
        }

        if (userRepository.findById(bookerId).isEmpty()) {
            throw new DataBaseNotFoundException("Пользователь не зарегистрирован, id пользователя: " + bookerId);
        }

        List<Booking> searchResult = new ArrayList<>();
        switch (bookingsState) {
            case ("ALL"):
                searchResult = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
                break;

            case ("WAITING"):
            case ("APPROVED"):
            case ("REJECTED"):
                searchResult = bookingRepository
                        .findAllByBookerIdAndStatusIgnoreCaseOrderByStartDesc(bookerId, bookingsState);
                break;

            case ("CURRENT"):
                searchResult = bookingRepository
                        .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, LocalDateTime.now(), LocalDateTime.now());

                break;
            case ("PAST"):
                searchResult = bookingRepository
                        .findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
                break;

            case ("FUTURE"):
                searchResult = bookingRepository
                        .findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
                break;

            default:
                log.info("Не найдены букинги по заданному параметру поиска: " + bookingsState);
                break;
        }
        return searchResult;
    }

    public List<Booking> bookingsForOwnerValidate(int ownerId, String bookingsState) {
        bookingsState = bookingsState.toUpperCase();
        if (!BOOKING_REQUEST_STATUS.contains(bookingsState)) {
            throw new ServerErrorException(bookingsState);
        }
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new DataBaseNotFoundException("Пользователь не зарегистрирован, id пользователя: " + ownerId);
        }

        List<Booking> searchResult = new ArrayList<>();
        switch (bookingsState) {
            case ("ALL"):
                searchResult = bookingRepository.findAllBookingsForOwner(ownerId);
                break;

            case ("WAITING"):
            case ("APPROVED"):
            case ("REJECTED"):
                searchResult = bookingRepository.findAllByOwnerIdAndByStatus(ownerId, bookingsState);
                break;

            case ("CURRENT"):
                searchResult = bookingRepository
                        .findCurrentBookingsForItemOwner(ownerId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case ("PAST"):
                searchResult = bookingRepository.findBookingsInPastForItemOwner(ownerId, LocalDateTime.now());

                break;
            case ("FUTURE"):
                searchResult = bookingRepository.findBookingsInFutureForItemOwner(ownerId, LocalDateTime.now());
                break;
            default:
                log.info("Не найдены букинги по заданному параметру поиска: " + bookingsState);
                break;
        }
        return searchResult;
    }

    public Item addLastAndNextBookingInformation(Item i) {
        Optional<Booking> booking = bookingRepository.getLastBooking(i.getOwnerId(), i.getId(), LocalDateTime.now());
        if (booking.isPresent()) {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setId(booking.get().getId());
            bookingDto.setBookerId(booking.get().getBookerId());
            bookingDto.setStart(booking.get().getStart());
            bookingDto.setEnd(booking.get().getEnd());
            i.setLastBooking(bookingDto);
        }

        Optional<Booking> book = bookingRepository.getNextBooking(i.getOwnerId(), i.getId(), LocalDateTime.now());
        if (book.isPresent()) {
            BookingDto bookingDto = new BookingDto();
            bookingDto.setId(book.get().getId());
            bookingDto.setBookerId(book.get().getBookerId());
            bookingDto.setStart(book.get().getStart());
            bookingDto.setEnd(book.get().getEnd());
            i.setNextBooking(bookingDto);
        }
        return i;
    }

    public List<Item> addLastAndNextBookingInformation(List<Item> itemlist) {
        for (Item i : itemlist) {
            addLastAndNextBookingInformation(i);
        }
        return itemlist;
    }
}
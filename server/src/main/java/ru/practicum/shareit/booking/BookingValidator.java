package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.DataBaseException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ServerErrorException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemValidator;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingValidator {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemValidator itemValidator;
    private final UserValidator userValidator;

    public void bookValidate(int bookerId, Booking book) {
        if (itemRepository.getByIdAndOwnerId(book.getItemId(), bookerId).isPresent()) {
            throw new NotFoundException("Владелец не может забронировать свою вещь.");
        }

        book.setBookerId(bookerId);
        /*
        if (book.getItemId() == null ||
                book.getEnd() == null ||
                book.getStart() == null ||
                book.getBookerId() == null) {
            throw new DataBaseException("Введены неполные данные при бронировании вещи.");
        }

         */

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
        int bookerId = bookingRepository.findById(bookingId).get().getBookerId();
        if (bookerId == ownerId) {
            throw new NotFoundException("Арендатор не может изменять статус заявки.");
        }

        if (bookingRepository.findById(bookingId)
                .get()
                .getStatus()
                .equalsIgnoreCase(String.valueOf(BookingState.APPROVED))) {
            throw new DataBaseException("Заявка  на букинг уже одобрена.");
        }
        userValidator.checkUserExists(ownerId);
        int itemId = bookingRepository.getById(bookingId).getItemId();

        itemRepository.getByIdAndOwnerId(itemId, ownerId)
                .orElseThrow(() -> new DataBaseException(
                        "Пользователь с id: " + ownerId + " не является владельцем вещи с id: " + itemId));

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
        bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найден букинг с id: " + bookingId));

        int itemId = bookingRepository.getById(bookingId).getItemId();
        if (bookingRepository.getByIdAndBookerId(bookingId, requestorId).isEmpty()
                && itemRepository.getByIdAndOwnerId(itemId, requestorId).isEmpty()) {
            throw new NotFoundException("Отсутствуют права на просмотр букинга");
        }
    }

    public List<Booking> bookingsSearchValidate(int bookerId, String bookingsState, Integer from, Integer size) {
        bookingsState = bookingsState.toUpperCase();
        if (!isInEnum(bookingsState)) {
            throw new ServerErrorException(bookingsState);
        }

        userRepository
                .findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не зарегистрирован, id пользователя: " + bookerId));

        //checkPagingParametersAreCorrect(from, size);
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("start").descending());

        List<Booking> searchResult = new ArrayList<>();
        switch (bookingsState) {
            case ("ALL"):

                searchResult = bookingRepository
                        .findAllByBookerIdFromStartElement(bookerId, from, size);
                break;

            case ("WAITING"):
            case ("APPROVED"):
            case ("REJECTED"):
                searchResult = bookingRepository
                        .findAllByBookerIdAndStatusIgnoreCase(bookerId, bookingsState, pageRequest);
                break;

            case ("CURRENT"):
                searchResult = bookingRepository
                        .findAllByBookerIdAndStartBeforeAndEndAfter(bookerId,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                pageRequest);

                break;
            case ("PAST"):
                searchResult = bookingRepository
                        .findAllByBookerIdAndEndBefore(bookerId,
                                LocalDateTime.now(),
                                pageRequest);
                break;

            case ("FUTURE"):
                searchResult = bookingRepository
                        .findAllByBookerIdAndStartAfter(bookerId,
                                LocalDateTime.now(),
                                pageRequest);
                break;

            default:
                log.info("Не найдены букинги по заданному параметру поиска: " + bookingsState);
                break;
        }
        return searchResult;
    }

    public List<Booking> bookingsForOwnerValidate(int ownerId,
                                                  String bookingsState,
                                                  Integer from,
                                                  Integer size) {
        bookingsState = bookingsState.toUpperCase();
        if (!isInEnum(bookingsState)) {
            throw new ServerErrorException(bookingsState);
        }

        userRepository
                .findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не зарегистрирован, id пользователя: " + ownerId));

        //checkPagingParametersAreCorrect(from, size);
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("start").descending());

        List<Booking> searchResult = new ArrayList<>();
        switch (bookingsState) {
            case ("ALL"):

                searchResult = bookingRepository
                        .findAllByOwnerIdFromStartElement(ownerId, from, size);
                break;

            case ("WAITING"):
            case ("APPROVED"):
            case ("REJECTED"):
                searchResult = bookingRepository.findAllByOwnerIdAndByStatus(ownerId, bookingsState, pageRequest);
                break;

            case ("CURRENT"):
                searchResult = bookingRepository
                        .findCurrentBookingsForItemOwner(ownerId,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                pageRequest);
                break;
            case ("PAST"):
                searchResult = bookingRepository.findBookingsInPastForItemOwner(ownerId,
                        LocalDateTime.now(),
                        pageRequest);

                break;
            case ("FUTURE"):
                searchResult = bookingRepository.findBookingsInFutureForItemOwner(ownerId,
                        LocalDateTime.now(),
                        pageRequest);
                break;
            default:
                log.info("Не найдены букинги по заданному параметру поиска: " + bookingsState);
                break;
        }

        return searchResult;
    }

    public Item addLastAndNextBookingInformation(Item i) {
       bookingRepository.getLastBooking(i.getOwnerId(),
                        i.getId(),
                        LocalDateTime.now(),
                        PageRequest.of(0, 1, Sort.by("start").descending()))
                .ifPresent(foundBooking -> {
                    BookingDto bookingDto = new BookingDto();
                    bookingDto.setId(foundBooking.getId());
                    bookingDto.setBookerId(foundBooking.getBookerId());
                    bookingDto.setStart(foundBooking.getStart());
                    bookingDto.setEnd(foundBooking.getEnd());
                    i.setLastBooking(bookingDto);
                });

        bookingRepository.getNextBooking(i.getOwnerId(),
                        i.getId(),
                        LocalDateTime.now(),
                        PageRequest.of(0, 1, Sort.by("start").ascending()))
                .ifPresent(foundBooking ->
                {
                    BookingDto bookingDto = new BookingDto();
                    bookingDto.setId(foundBooking.getId());
                    bookingDto.setBookerId(foundBooking.getBookerId());
                    bookingDto.setStart(foundBooking.getStart());
                    bookingDto.setEnd(foundBooking.getEnd());
                    i.setNextBooking(bookingDto);
                });
        return i;
    }

    public List<Item> addLastAndNextBookingInformation(List<Item> itemlist) {
        for (Item i : itemlist) {
            addLastAndNextBookingInformation(i);
        }
        return itemlist;
    }

    public boolean isInEnum(String value) {
        return Arrays.stream(BookingSearchParameters.values()).anyMatch(e -> e.name().equals(value));
    }
/*
    private void checkPagingParametersAreCorrect(Integer from, Integer size) {
        if (size == null || size <= 0) {
            throw new DataBaseException("Количество объектов, подлежащих выводу на одной странице, должно быть положительным.");
        }
        if (from < 0) {
            throw new DataBaseException("Номер начальной страницы не может быть отрицательным.");
        }
    }

 */
}
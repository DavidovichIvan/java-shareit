package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.DataBaseException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemValidator {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    public void itemValidate(int ownerId, Item item) {
        if (item.getName() == null ||
                item.getName().isBlank() ||
                item.getDescription() == null ||
                item.getDescription().isBlank() ||
                item.getAvailable() == null) {
            throw new DataBaseException("Введены неполные данные при добавлении новой вещи.");
        }
        User u = userRepository
                .findById(ownerId).orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + ownerId));

    }

    public void prepareItemToUpdate(int itemId, int userId, Item updItem) {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + itemId));


        if (updItem.getName() == null || updItem.getName().isBlank()) {
            updItem.setName(oldItem.getName());
        }

        if (updItem.getDescription() == null || updItem.getDescription().isBlank()) {
            updItem.setDescription(oldItem.getDescription());
        }

        if (updItem.getAvailable() == null) {
            updItem.setAvailable(oldItem.getAvailable());
        }

        updItem.setId(itemId);
        updItem.setOwnerId(userId);
    }

    public void checkItemExists(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Не найдена вещь с id: " + itemId);
        }
    }

    public boolean validateSearchParameters(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return false;
        }
        return true;
    }

    public Comment commentValidateAndCreate(int itemId, int authorId, String text) {
        if (text == null || text.isBlank()) {
            throw new DataBaseException("Введен пустой комментарий");
        }
        Booking book = bookingRepository.getByIdAndBookerId(itemId, authorId).orElseThrow(() -> new DataBaseException
                ("Пользователь не может оставить отзыв так как он не арендовал вещь с id: " + itemId));

        Comment comment = new Comment();
        comment.setItemId(itemId);
        comment.setAuthorId(authorId);
        comment.setText(text);
        comment.setAuthorName(userRepository.findById(authorId).get().getName());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public boolean checkOwnerOrNot(int requesterId, int itemId) {
        Optional<Item> item = itemRepository.getByIdAndOwnerId(itemId, requesterId);
        return item.isPresent();
    }
}
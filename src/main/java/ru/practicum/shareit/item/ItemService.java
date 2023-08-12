package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingValidator;

import java.util.Collections;
import java.util.List;

@Slf4j
@Getter
@Setter
@Service
@RequiredArgsConstructor
@Transactional
//@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
   // private final UserRepository userRepository; //хз возможно не понадобится
    private final ItemValidator itemValidator;
    private final BookingValidator bookingValidator;

    @Transactional
    public Item addItem(int ownerId, Item item) {
        log.info("Запрос на добавление вещи");
        itemValidator.itemValidate(ownerId, item);

        item.setOwnerId(ownerId);
        return itemRepository.save(item);
    }

    @Transactional
    public Item updItem(int itemId, int userId, Item updItem) {
        log.info("Запрос на обновление вещи с id: " + itemId);
        itemValidator.prepareItemToUpdate(itemId, userId, updItem);

        return itemRepository.save(updItem);
    }

    public List<Item> getUserItems(int ownerId) {
        log.info("Запрос на просмотр всех вещей пользователя c id: " + ownerId);
      // return repository.findByOwnerId(ownerId); (так просто весь список возвращает вещей для пользователя)
    return bookingValidator.addLastAndNextBookingInformation(itemRepository.findByOwnerId(ownerId));
    }

    public Item getItemById(int requesterId, int itemId) {
        log.info("Запрос на просмотр вещи с id: " + itemId);
        itemValidator.checkItemExists(itemId);

        //проверить если не собственник то просто ретурн
        if(!itemValidator.checkOwnerOrNot(requesterId, itemId))
        {
            return itemRepository.findById(itemId).get();
        }

        //если собственник то вот этот ретурн остается
        return bookingValidator
                .addLastAndNextBookingInformation(itemRepository.findById(itemId).get());

    }

    public List<Item> searchItem(String searchName, String searchDescription) {
        log.info("Запрос на поиск вещи");
        if (!itemValidator.validateSearchParameters(searchName)) {
            return Collections.emptyList();
        }

        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(searchName, searchDescription);
    }
    @Transactional
     public Comment addComment(int itemId, int userId, String text) {
        log.info("Запрос на добавление комментария");
        Comment comment = itemValidator.commentValidateAndCreate(itemId, userId, text);

        return commentRepository.save(comment);
     }

}
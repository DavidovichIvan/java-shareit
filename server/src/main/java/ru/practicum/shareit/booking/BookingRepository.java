package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> getByIdAndBookerId(int id, int bookerId);

    List<Booking> findAllByBookerIdAndStatusIgnoreCase(int bookerId,
                                                       String status,
                                                       PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartAfter(int bookerId,
                                                 LocalDateTime time,
                                                 PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndEndBefore(int bookerId,
                                                LocalDateTime time,
                                                PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(int bookerId,
                                                             LocalDateTime start,
                                                             LocalDateTime end,
                                                             PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id and i.ownerId = :ownerId and upper(b.status) like concat(:status,'%')")
    List<Booking> findAllByOwnerIdAndByStatus(int ownerId, String status, PageRequest pageRequest);


    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id and i.ownerId = :ownerId and b.start > :time")
    List<Booking> findBookingsInFutureForItemOwner(int ownerId,
                                                   LocalDateTime time,
                                                   PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id and i.ownerId = :ownerId and b.end < :time")
    List<Booking> findBookingsInPastForItemOwner(int ownerId,
                                                 LocalDateTime time,
                                                 PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id and i.ownerId = ?1 and b.start < ?2 and b.end > ?3")
    List<Booking> findCurrentBookingsForItemOwner(int ownerId,
                                                  LocalDateTime start,
                                                  LocalDateTime end,
                                                  PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id\n" +
            "and i.ownerId = :ownerId and b.itemId = :itemId and b.start < :now and b.status != 'REJECTED'")
    Optional<Booking> getLastBooking(int ownerId, int itemId, LocalDateTime now, PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id\n" +
            "and i.ownerId = :ownerId and b.itemId = :itemId and b.start > :now and b.status != 'REJECTED'")
    Optional<Booking> getNextBooking(int ownerId, int itemId, LocalDateTime now, PageRequest pageRequest);


    //альтернатива явной пагинации, как пример в одном методе использовал
    @Query(value = "select * from Booking\n" +
            "where booker_id = :bookerId\n" +
            "order by start_date desc \n" +
            "offset :startElement fetch next :elementsPerPage rows only", nativeQuery = true)
    List<Booking> findAllByBookerIdFromStartElement(int bookerId,
                                                    int startElement,
                                                    int elementsPerPage);

    @Query(value = "select * from Booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "where i.owner_id = :ownerId\n" +
            "order by start_date desc \n" +
            "offset :startElement fetch next :elementsPerPage rows only", nativeQuery = true)
    List<Booking> findAllByOwnerIdFromStartElement(int ownerId,
                                                   int startElement,
                                                   int elementsPerPage);
}
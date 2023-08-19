package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Optional<Booking> getByIdAndBookerId(int id, int bookerId);

    List<Booking> findAllByBookerIdAndStatusIgnoreCaseOrderByStartDesc(int bookerId,
                                                                       String status,
                                                                       PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(int bookerId,
                                                                 LocalDateTime time,
                                                                 PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(int bookerId,
                                                                LocalDateTime time,
                                                                PageRequest pageRequest);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int bookerId,
                                                                             LocalDateTime start,
                                                                             LocalDateTime end,
                                                                             PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id and i.ownerId = :ownerId order by b.start desc")
    List<Booking> findAllBookingsForOwner(int ownerId);

    @Query(value = "SELECT * FROM Booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = :ownerId AND UPPER (b.status) LIKE CONCAT(:status,'%')\n" +
            "ORDER BY b.START_DATE DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdAndByStatus(int ownerId, String status, PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id and i.ownerId = :ownerId and b.start > :time\n" +
            "order by b.start desc")
    List<Booking> findBookingsInFutureForItemOwner(int ownerId,
                                                   LocalDateTime time,
                                                   PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id and i.ownerId = :ownerId and b.end < :time\n" +
            "order by b.start desc")
    List<Booking> findBookingsInPastForItemOwner(int ownerId,
                                                 LocalDateTime time,
                                                 PageRequest pageRequest);

    @Query(value = "select b, i from Booking b, Item i\n" +
            "where b.itemId = i.id and i.ownerId = ?1 and b.start < ?2 and b.end > ?3\n" +
            "order by b.start desc")
    List<Booking> findCurrentBookingsForItemOwner(int ownerId,
                                                  LocalDateTime start,
                                                  LocalDateTime end,
                                                  PageRequest pageRequest);

    @Query(value = "SELECT * FROM booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = :ownerId AND b.ITEM_ID = :itemId AND b.START_DATE < :now AND b.STATUS != 'REJECTED'\n" +
            "ORDER BY b.START_DATE DESC\n" +
            "FETCH NEXT 1 ROWS ONLY\n", nativeQuery = true)
    Optional<Booking> getLastBooking(int ownerId, int itemId, LocalDateTime now);

    @Query(value = "SELECT * FROM booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = :ownerId AND b.ITEM_ID = :itemId AND b.START_DATE > :now AND b.STATUS != 'REJECTED' \n" +
            "ORDER BY b.START_DATE ASC\n" +
            "FETCH NEXT 1 ROWS ONLY", nativeQuery = true)
    Optional<Booking> getNextBooking(int ownerId, int itemId, LocalDateTime now);

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
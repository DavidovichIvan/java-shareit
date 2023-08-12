package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Modifying
    @Query(value = "update booking b set status = ?1 where booking_id = ?2", nativeQuery = true)
    void updateStatusById(String status, Integer bookingId);

    Optional<Booking> getByIdAndBookerId(int id, int bookerId);

    List<Booking> findAllByBookerIdOrderByStartDesc(int bookerId);

    List<Booking> findAllByStatusIgnoreCaseOrderByStartDesc(String status);

    List<Booking> findAllByBookerIdAndStatusIgnoreCaseOrderByStartDesc(int bookerId, String status);

    List<Booking> findAllByStartAfterOrderByStartDesc(LocalDateTime time);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(int bookerId, LocalDateTime time);

    //переделал для тестов на время окончания не наступило
    List<Booking> findAllByEndAfterOrderByStartDesc(LocalDateTime time);

    List<Booking> findAllByEndBeforeOrderByStartDesc(LocalDateTime time);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(int bookerId, LocalDateTime time);

    List<Booking> findAllByStartBeforeAndEndAfterOrderByStartDesc(LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(int bookerId, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT * FROM booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = ?1 ORDER BY b.START_DATE DESC", nativeQuery = true)
    List<Booking> findAllBookingsForOwner(int ownerId);

    @Query(value = "SELECT * FROM Booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = ?1 AND UPPER (b.status) LIKE CONCAT(?2,'%')\n" +
            "ORDER BY b.START_DATE DESC", nativeQuery = true)
    List<Booking> findAllByOwnerIdAndByStatus(int ownerId, String status);

    @Query(value = "SELECT * FROM Booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = ?1 AND b.START_DATE > ?2\n" +
            "ORDER BY b.START_DATE DESC", nativeQuery = true)
    List<Booking> findBookingsInFutureForItemOwner(int ownerId, LocalDateTime time);


    @Query(value = "SELECT * FROM Booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = ?1 AND b.END_DATE < ?2\n" +
            "ORDER BY b.START_DATE DESC", nativeQuery = true)
    List<Booking> findBookingsInPastForItemOwner(int ownerId, LocalDateTime time);

    @Query(value = "SELECT * FROM Booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = ?1 AND b.START_DATE < ?2 AND b.END_DATE > ?3\n" +
            "ORDER BY b.START_DATE DESC", nativeQuery = true)
    List<Booking> findCurrentBookingsForItemOwner(int ownerId, LocalDateTime start, LocalDateTime end);


    @Query(value = "SELECT * FROM booking b\n" +
            "INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "WHERE i.OWNER_ID = ?1 AND b.ITEM_ID = ?2 AND b.START_DATE < ?3 AND b.STATUS != 'REJECTED'\n" +
            "ORDER BY b.START_DATE DESC\n" +
            "FETCH NEXT 1 ROWS ONLY\n", nativeQuery = true)
    Optional<Booking> getLastBooking(int ownerId, int itemId, LocalDateTime now);

    @Query(value = "SELECT * FROM booking b\n" +
            "            INNER JOIN ITEMS i ON b.ITEM_ID = i.ITEM_ID\n" +
            "          WHERE i.OWNER_ID = ?1 AND b.ITEM_ID = ?2 AND b.START_DATE > ?3 AND b.STATUS != 'REJECTED' \n" +
            "           ORDER BY b.START_DATE ASC\n" +
            "FETCH NEXT 1 ROWS ONLY", nativeQuery = true)
    Optional<Booking> getNextBooking(int ownerId, int itemId, LocalDateTime now);
}
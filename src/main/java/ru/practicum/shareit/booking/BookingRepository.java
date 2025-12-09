package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId ORDER BY b.start DESC")
    List<Booking> findByOwnerId(@Param("ownerId") Long ownerId);

    List<Booking> findByItemIdAndStatusAndEndBeforeOrderByEndDesc(
            Long itemId, Status status, LocalDateTime time);

    List<Booking> findByItemId(Long itemId);

    List<Booking> findByItemIdAndStatusAndStartAfterOrderByStartAsc(
            Long itemId, Status status, LocalDateTime time);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId " +
            "AND b.booker.id = :bookerId " +
            "AND b.status = :status " +
            "AND b.end < :currentTime")
    List<Booking> findByItemIdAndBookerIdAndStatusAndEndBefore(
            @Param("itemId") Long itemId,
            @Param("bookerId") Long bookerId,
            @Param("status") Status status,
            @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "AND b.status = :status " +
            "AND b.end < :now " +
            "AND b.id IN (" +
            "   SELECT MAX(b2.id) FROM Booking b2 " +
            "   WHERE b2.item.id = b.item.id " +
            "   AND b2.status = :status " +
            "   AND b2.end < :now " +
            "   GROUP BY b2.item.id" +
            ")")
    List<Booking> findLastBookingsForItems(@Param("itemIds") List<Long> itemIds,
                                           @Param("status") Status status,
                                           @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "AND b.status = :status " +
            "AND b.start > :now " +
            "AND b.id IN (" +
            "   SELECT MIN(b2.id) FROM Booking b2 " +
            "   WHERE b2.item.id = b.item.id " +
            "   AND b2.status = :status " +
            "   AND b2.start > :now " +
            "   GROUP BY b2.item.id" +
            ")")
    List<Booking> findNextBookingsForItems(@Param("itemIds") List<Long> itemIds,
                                           @Param("status") Status status,
                                           @Param("now") LocalDateTime now);
}
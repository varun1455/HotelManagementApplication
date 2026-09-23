package com.project.stayEase.repository;

import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.Room;
import com.project.stayEase.configuration.projection.RoomAvailabilityProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByRoom(Room room);


    /* Searching Hotels with Available Inventory in given date range with rooms count */
    @Query("""
           SELECT DISTINCT i.hotel
           FROM Inventory i
           WHERE i.city = :city
                AND i.date BETWEEN :startDate AND :endDate
                AND i.closed = false
                AND (i.totalCount - i.bookedCount-i.reservedCount) >= :roomsCount
           GROUP BY i.hotel, i.room
           HAVING COUNT(i.date) = :totalNights
           """)
    Page<Hotel> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("totalNights") Long totalNights,
            Pageable pageable
    );

    @Query("""
            SELECT
                i.hotel.id AS hotelId,
                i.room AS room,
                MIN(i.totalCount - i.bookedCount - i.reservedCount) AS availableRooms,
                SUM(i.price) As totalPrice
            FROM Inventory i
            WHERE i.hotel.id IN :hotelIds
                AND i.date BETWEEN :startDate AND :endDate
                AND i.closed = false
            GROUP BY i.hotel, i.room
            HAVING
                COUNT(i.date) = :totalNights
                AND MIN(i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount
            
""") List<RoomAvailabilityProjection> findAvailableRooms(
        @Param("hotelIds") List<Long> hotelIds,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("roomsCount") Integer roomsCount,
        @Param("totalNights") Long totalNights

    );

    /* Applying Lock on rooms which user have to book  */
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.room.id = :roomId
                AND i.date >= :startDate
                AND i.date < :endDate
                AND i.closed = false
                AND (i.totalCount - i.bookedCount- i.reservedCount) >= :roomsCount
            ORDER BY i.date ASC
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockAvailableInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount
    );


    /* initialize booking so increase the reserved count of rooms which user want to book */
    @Modifying
    @Query("""
        UPDATE Inventory i
        SET i.reservedCount = i.reservedCount + :numberOfRooms
        WHERE i.room.id = :roomId
        AND i.date >= :startDate
        AND i.date < :endDate
        AND i.closed = false
 
 """)void initBooking(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfRooms") Integer numberOfRooms
    );

    /* Releasing Inventory for expiredBookings i.e. decreasing the reservedCount of expiredBookings */
    @Modifying
    @Query("""
       UPDATE Inventory i
       SET i.reservedCount = i.reservedCount - :numberOfRooms
       WHERE i.room.id = :roomId
         AND i.date >= :startDate
         AND i.date < :endDate
         AND i.reservedCount >= :numberOfRooms
         AND i.closed = false
""")
    void releasedInventoryForExpiredBooking(
    @Param("roomId") Long roomId,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate,
    @Param("numberOfRooms") Integer numberOfRooms);



    /* Applying lock on rooms if user is instantiating s payment so that those rooms booking will be confirmed */
    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.room.id = :roomId
                AND i.date >= :startDate
                AND i.date < :endDate
                AND (i.totalCount - i.bookedCount) >= :roomsCount
                AND i.closed = false
            ORDER BY i.date ASC
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockReservedInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount
    );


    /* Making Booking confirmation with increasing the bookedCount and decreasing the reservedCount of rooms */
    @Modifying
    @Query("""
               UPDATE Inventory i
                SET i.reservedCount = i.reservedCount - :numberOfRooms,
                    i.bookedCount = i.bookedCount + :numberOfRooms
                WHERE i.room.id = :roomId
                    AND i.date >= :startDate
                    AND i.date < :endDate
                    AND i.reservedCount >= :numberOfRooms
                    AND i.closed = false
    """
    )
    void confirmBooking(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfRooms") Integer numberOfRooms
    );


    /* Modifying the bookedCount if user is cancelling the confirmed booking */
    @Modifying
    @Query("""
            UPDATE Inventory i
            SET i.bookedCount = i.bookedCount - :numberOfRooms
            WHERE i.room.id = :roomId
              AND i.date >= :startDate
              AND i.date < :endDate
              AND i.bookedCount >= :numberOfRooms
              AND i.closed = false
""")int cancelBooking(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfRooms") Integer numberOfRooms
    );



    @Query("""
             SELECT MAX(i.date)
             FROM Inventory i
             WHERE i.room.hotel.id = :hotelId
             AND i.dynamicPrice IS NOT NULL
""")
    LocalDate findLastDynamicPriceDateByHotel(@Param("hotelId") Long hotelId);


    @Query("""
    SELECT MAX(i.date)
    FROM Inventory i
    WHERE i.dynamicPrice IS NOT NULL
""")
    LocalDate findLastDynamicPriceDate();


    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);


    @Query("""
     SELECT i
     FROM Inventory i
     WHERE i.date IN :dates
""")
    List<Inventory> findByDates(@Param("dates") List<LocalDate> dates);

    List<Inventory> findByRoomAndDateBetween(Room room, LocalDate startDate, LocalDate endDate);

    @Query("""
    SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
    FROM Inventory i
    WHERE i.room = :room
      AND i.date >= :today
      AND i.bookedCount > :totalCount
""")
    boolean existsBookedCountGreaterThan(
            @Param("room") Room room,
            @Param("today") LocalDate today,
            @Param("totalCount") Integer totalCount
    );
    List<Inventory> findByRoomAndDateGreaterThanEqual(Room room, LocalDate today);
}
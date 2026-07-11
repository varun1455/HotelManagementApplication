package com.project.stayEase.repository;

import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndUserId(Long bookingId, Long userId);

    @Query("""
        SELECT b
        FROM Booking b
        where b.bookingStatus = :status
        AND b.reservedUntil <= :currentTime
""")
    List<Booking> findExpiredBookings(
            @Param("status")BookingStatus status,
            @Param("currentTime") LocalDateTime currentTime
    );

    @Query("""
        SELECT b
        FROM Booking b
        where b.bookingStatus = :status
        AND b.paymentInitiatedAt <= :currentTime
""")
    List<Booking> findPaymentPendingBookings(
            @Param("status")BookingStatus status,
            @Param("currentTime") LocalDateTime currentTime
    );

}

package com.project.stayEase.repository;

import com.project.stayEase.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndUserId(Long bookingId, Long userId);
}

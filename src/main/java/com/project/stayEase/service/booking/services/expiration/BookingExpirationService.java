package com.project.stayEase.service.booking.services.expiration;

import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.BookingRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingExpirationService {

    private final BookingRepository bookingRepository;
    private final InventoryRepository inventoryRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void expireBookings() {
        expireReservedBookings();
        expirePaymentPendingBookings();
    }

    private void expireReservedBookings() {

        List<BookingStatus> expirableStatuses = List.of(
                BookingStatus.RESERVED,
                BookingStatus.GUESTS_ADDED
        );

        List<Booking> bookings = bookingRepository.findExpiredBookings(
                expirableStatuses, LocalDateTime.now());

        for (Booking booking : bookings) {
            expireBooking(booking);
        }
    }

    private void expirePaymentPendingBookings() {

        List<Booking> bookings =
                bookingRepository.findPaymentPendingBookings(
                        BookingStatus.PAYMENT_PENDING, LocalDateTime.now().minusMinutes(15));

        for (Booking booking : bookings) {
            failPendingPayment(booking.getId());
            expireBooking(booking);
        }
    }

    private void expireBooking(Booking booking) {
        inventoryRepository.releasedInventoryForExpiredBooking(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
        booking.setBookingStatus(BookingStatus.EXPIRED);
    }


    private void failPendingPayment(Long bookingId) {

        paymentRepository
                .findFirstByBookingIdAndPaymentStatusOrderByCreatedAtDesc(
                        bookingId,
                        PaymentStatus.PENDING
                )
                .ifPresent(payment -> {
                    payment.setPaymentStatus(PaymentStatus.FAILED);
                });
    }


}

package com.project.stayEase.schedulers.booking;


import com.project.stayEase.service.booking.services.expiration.BookingExpirationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpireBookingsScheduler {
    private final BookingExpirationService bookingExpirationService;

    @Scheduled(cron = "0 * * * * *")
    public void expireBookings() {
        bookingExpirationService.expireBookings();
    }

}

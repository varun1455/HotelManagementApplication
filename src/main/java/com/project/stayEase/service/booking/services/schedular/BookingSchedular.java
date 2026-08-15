package com.project.stayEase.service.booking.services.schedular;


import com.project.stayEase.service.booking.services.expiration.BookingExpirationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingSchedular {
    private final BookingExpirationService bookingExpirationService;

    @Scheduled(cron = "0 * * * * *")
    public void expireBookings() {
        bookingExpirationService.expireBookings();
    }

}

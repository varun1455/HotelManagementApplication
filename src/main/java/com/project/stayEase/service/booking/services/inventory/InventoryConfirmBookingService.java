package com.project.stayEase.service.booking.services.inventory;

import com.project.stayEase.customExceptions.InvalidBookingStateException;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryConfirmBookingService {

    private final InventoryRepository inventoryRepository;;

    public void confirm(Booking booking) {

        if (booking.getBookingStatus() != BookingStatus.PAYMENT_PENDING) {
            throw new InvalidBookingStateException("Booking cannot be confirmed from state " + booking.getBookingStatus());
        }

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomsCount());

        inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomsCount());

        booking.setBookingStatus(BookingStatus.CONFIRMED);
    }

}

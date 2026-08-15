package com.project.stayEase.service.booking.services.inventory;


import com.project.stayEase.customExceptions.InventoryUpdateException;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class InventoryReleaseService {

    private final InventoryRepository inventoryRepository;

    public void releaseInventory(Booking booking){
        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(), booking.getRoomsCount());
        int rowsEffected = inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(), booking.getRoomsCount());
        int totalDays = Math.toIntExact(ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate()));
        if(rowsEffected != totalDays ){
            throw new InventoryUpdateException("Inventory update failed");
        }
    }
}

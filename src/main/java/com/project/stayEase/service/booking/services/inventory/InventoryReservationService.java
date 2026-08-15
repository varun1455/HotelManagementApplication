package com.project.stayEase.service.booking.services.inventory;


import com.project.stayEase.customExceptions.RoomNotAvailableException;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryReservationService {

    private final InventoryRepository inventoryRepository;

    public List<Inventory> reserveInventoryForBooking(Long roomId, LocalDate checkInDate, LocalDate checkOutDate, Integer roomsCount){

            List<Inventory> inventories = inventoryRepository.findAndLockAvailableInventory(roomId, checkInDate, checkOutDate, roomsCount);
            long expectedDays = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            if (inventories.size() != expectedDays) {
                throw new RoomNotAvailableException("Room is no longer available");
            }

        inventoryRepository.initBooking(
                roomId,
                checkInDate,
                checkOutDate,
                roomsCount);

        return inventories;

    }
}

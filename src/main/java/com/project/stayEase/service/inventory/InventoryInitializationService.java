package com.project.stayEase.service.inventory;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.Room;
import com.project.stayEase.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryInitializationService {

    private final InventoryRepository inventoryRepository;

    public void initialize(Room room) {


        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusMonths(6);

        List<Inventory> inventories = new ArrayList<>();

        for (; !today.isAfter(endDate); today = today.plusDays(1)) {
            inventories.add(

                    Inventory.builder()
                            .hotel(room.getHotel())
                            .room(room)
                            .bookedCount(0)
                            .reservedCount(0)
                            .city(room.getHotel().getCity())
                            .date(today)
                            .price(room.getBasePrice())
                            .totalCount(room.getTotalCount())
                            .closed(false)
                            .build()

            );

        }

        inventoryRepository.saveAll(inventories);
    }


    @Transactional
    public void updateInventory(Room room, BigDecimal basePrice, Integer totalCount) {
        LocalDate today = LocalDate.now();

        boolean invalid =
                inventoryRepository.existsBookedCountGreaterThan(room, today, totalCount);

        if (invalid) {
            throw new IllegalArgumentException(
                    "Cannot reduce room quantity below the number of already booked rooms"
            );
        }

        List<Inventory> inventories =
                inventoryRepository.findByRoomAndDateGreaterThanEqual(room, today);

        for (Inventory inventory : inventories) {
            inventory.setTotalCount(totalCount);
            inventory.setPrice(basePrice);
        }

        inventoryRepository.saveAll(inventories);


    }
}

package com.project.stayEase.service.inventory;


import com.project.stayEase.entity.Room;
import com.project.stayEase.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryInitializationService inventoryInitializationService;

    public void initializeRoom(Room room) {
        inventoryInitializationService.initialize(room);
    }
    public void deleteInventories(Room room) {
        inventoryRepository.deleteByRoom(room);
    }

    public void updateFutureInventory(Room room, BigDecimal basePrice, Integer totalCount) {
        inventoryInitializationService.updateInventory(room, basePrice, totalCount);
    }
}

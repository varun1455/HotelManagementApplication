package com.project.stayEase.service;

import com.project.stayEase.entity.Room;

public interface InventoryService {

    void initializeRoomForYear(Room room);

    void deleteAllInventoriesForRoom(Room room);


}

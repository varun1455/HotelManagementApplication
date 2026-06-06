package com.project.stayEase.service;

import com.project.stayEase.dto.HotelInfoDto;
import com.project.stayEase.dto.HotelResponseDto;
import com.project.stayEase.dto.HotelSearchRequestDto;
import com.project.stayEase.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForHalfYear(Room room);

    void deleteAllInventoriesForRoom(Room room);

    Page<?> searchHotels(HotelSearchRequestDto hotelSearchRequestDto);
}

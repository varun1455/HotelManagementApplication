package com.project.stayEase.service.search.projection;

import com.project.stayEase.entity.Room;

import java.math.BigDecimal;

public interface RoomAvailabilityProjection {

    Long getHotelId();
    Room getRoom();
    Integer getAvailableRooms();
    BigDecimal getTotalPrice();
}

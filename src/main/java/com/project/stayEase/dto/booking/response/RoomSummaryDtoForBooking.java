package com.project.stayEase.dto.booking.response;

import lombok.Data;

@Data
public class RoomSummaryDtoForBooking {
    private Long id;
    private Long roomTypeId;
    private Long bedTypeId;
    private String[] photos;
    private String[] amenities;
}

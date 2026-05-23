package com.project.stayEase.service;

import com.project.stayEase.dto.RoomRequestDto;
import com.project.stayEase.dto.RoomResponseDto;

import java.util.List;

public interface RoomService {

    RoomResponseDto createRoom(Long hotelId, RoomRequestDto roomRequestDto);

    List<RoomResponseDto> getAllRoomsInHotel(Long hotelId);

    RoomResponseDto getRoomById(Long roomId);

    RoomResponseDto updateRoom(Long roomId, RoomRequestDto roomRequestDto);

    void deleteRoomById(long roomId);
}

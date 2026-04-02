package com.project.stayEase.service;

import com.project.stayEase.dto.RoomTypeRequestDto;
import com.project.stayEase.dto.RoomTypeResponseDto;

import java.util.List;

public interface RoomTypeService {

    RoomTypeResponseDto createRoomType(RoomTypeRequestDto roomTypeRequestDto);

    RoomTypeResponseDto findRoomType(Long roomTypeId);

    List<RoomTypeResponseDto> findAllRoomTypes();
}

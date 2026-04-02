package com.project.stayEase.service;

import com.project.stayEase.dto.HotelRequestDto;
import com.project.stayEase.dto.HotelResponseDto;

public interface HotelService {

    HotelResponseDto createNewHotel(HotelRequestDto hotelRequestDto);

    HotelResponseDto getHotelById(Long id);

    HotelResponseDto updateHotelById(Long id,  HotelRequestDto hotelRequestDto );

    void activateHotel(Long id);
}

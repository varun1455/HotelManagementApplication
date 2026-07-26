package com.project.stayEase.service;

import com.project.stayEase.dto.HotelInfoDto;
import com.project.stayEase.dto.HotelRequestDto;
import com.project.stayEase.dto.HotelResponseDto;

import java.util.List;

public interface HotelService {

    HotelResponseDto createNewHotel(HotelRequestDto hotelRequestDto);

    HotelResponseDto getHotelById(Long id);

    HotelResponseDto updateHotelById(Long id,  HotelRequestDto hotelRequestDto );

    void activateHotel(Long id);

    void deleteHotelById(Long id);

    HotelInfoDto findHotelInfo(Long hotelId);

    List<HotelResponseDto> getAllHotelsOfOwner();
}

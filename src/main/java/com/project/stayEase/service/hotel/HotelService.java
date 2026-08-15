package com.project.stayEase.service.hotel;

import com.project.stayEase.dto.hotelMappers.HotelInfoDto;
import com.project.stayEase.dto.hotelMappers.HotelRequestDto;
import com.project.stayEase.dto.hotelMappers.HotelResponseDto;
import com.project.stayEase.entity.Hotel;

import java.util.List;

public interface HotelService {

    HotelResponseDto createNewHotel(HotelRequestDto hotelRequestDto);

    HotelResponseDto getHotelById(Long id);

    HotelResponseDto updateHotelById(Long id,  HotelRequestDto hotelRequestDto );

    void activateHotel(Long id);

    void deleteHotelById(Long id);

    HotelInfoDto findHotelInfo(Long hotelId);

    List<HotelResponseDto> getAllHotelsOfOwner();

    Hotel getActiveHotel(Long id);
}

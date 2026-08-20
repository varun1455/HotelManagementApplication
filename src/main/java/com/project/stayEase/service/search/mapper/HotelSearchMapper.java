package com.project.stayEase.service.search.mapper;

import com.project.stayEase.dto.hotelSearchMappers.HotelSearchResponseDto;
import com.project.stayEase.dto.hotelSearchMappers.RoomSearchResponseDto;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.service.search.projection.HotelSearchProjection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HotelSearchMapper {

    private final ModelMapper modelMapper;

    public HotelSearchResponseDto mapHotel(Hotel hotel, BigDecimal startingPrice){
        HotelSearchResponseDto dto =
                modelMapper.map(hotel, HotelSearchResponseDto.class);
        dto.setStartingFrom(startingPrice);

        return dto;
    }

    public HotelSearchResponseDto toDynamicResponse(HotelSearchProjection projection){
        HotelSearchResponseDto dto = mapHotel(projection.hotel(), projection.startingFrom());
        dto.setRooms(Collections.emptyList());
        return dto;
    }

    public HotelSearchResponseDto toBaseResponse(Hotel hotel,
                                                 BigDecimal startingPrice,
                                                 List<RoomSearchResponseDto> rooms){
        HotelSearchResponseDto dto =
                mapHotel(hotel, startingPrice);

        dto.setRooms(rooms);

        return dto;
    }

}

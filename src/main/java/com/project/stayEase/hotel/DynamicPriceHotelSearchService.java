package com.project.stayEase.hotel;


import com.project.stayEase.dto.HotelSearchRequestDto;
import com.project.stayEase.dto.HotelSearchResponseDto;
import com.project.stayEase.hotel.mapper.HotelSearchMapper;
import com.project.stayEase.repository.HotelMinPriceRepository;
import com.project.stayEase.hotel.projection.HotelSearchProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicPriceHotelSearchService {

    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final HotelSearchMapper hotelSearchMapper;


    public Page<HotelSearchResponseDto> search(HotelSearchRequestDto hotelSearchRequestDto){

        Pageable pageable = PageRequest.of(hotelSearchRequestDto.getPage(), hotelSearchRequestDto.getSize());
        Page<HotelSearchProjection>  projections =  hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequestDto.getCity(), hotelSearchRequestDto.getStartDate(),
                hotelSearchRequestDto.getEndDate(), pageable);

//        Map<Long, BigDecimal> startingPriceMap = projections.getContent().stream()
//                .collect(Collectors.toMap(
//                                hotelSearchProjection -> hotelSearchProjection.hotel().getId(),
//                                HotelSearchProjection::startingFrom
//                        )
//                );

//        List<Long> hotelIds = projections.getContent()
//                .stream()
//                .map(HotelSearchProjection::hotelId)
//                .toList();

//        List<Hotel> hotels = hotelRepository.findAllById(hotelIds);

        // List<HotelMinPriceSearchResponseDto> hotelMinPriceSearchResponse = hotels.stream()
        //         .map(hotel-> {
        //             HotelMinPriceSearchResponseDto dto = modelMapper.map(hotel, HotelMinPriceSearchResponseDto.class);
        //             dto.setStartingFrom(startingPriceMap.get(hotel.getId()));
        //             return dto;

        //         }).toList();

        List<HotelSearchResponseDto> hotelSearchResponse = projections.getContent().stream()
        .map(hotelSearchMapper::toDynamicResponse).toList();

        return new PageImpl<>(hotelSearchResponse, pageable, projections.getTotalElements());
    }


}

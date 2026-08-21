package com.project.stayEase.service.search;


import com.project.stayEase.dto.search.request.HotelSearchRequestDto;
import com.project.stayEase.dto.search.response.HotelSearchResponseDto;
import com.project.stayEase.configuration.mapper.HotelSearchMapper;
import com.project.stayEase.repository.HotelMinPriceRepository;
import com.project.stayEase.configuration.projection.HotelSearchProjection;
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

        List<HotelSearchResponseDto> hotelSearchResponse = projections.getContent().stream()
        .map(hotelSearchMapper::toDynamicResponse).toList();

        return new PageImpl<>(hotelSearchResponse, pageable, projections.getTotalElements());
    }


}

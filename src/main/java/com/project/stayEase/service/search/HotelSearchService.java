package com.project.stayEase.service.search;

import com.project.stayEase.dto.search.request.HotelSearchRequestDto;
import com.project.stayEase.dto.search.response.HotelSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class HotelSearchService {

        private final DynamicPriceHotelSearchService dynamicPriceHotelSearchService;
        private final BasePriceHotelSearchService basePriceHotelSearchService;

        public Page<HotelSearchResponseDto> search(HotelSearchRequestDto hotelSearchRequestDto) {

            int daysUntilCheckIn = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), hotelSearchRequestDto.getStartDate()));

            if(daysUntilCheckIn <= 60){
                return dynamicPriceHotelSearchService.search(hotelSearchRequestDto);
            }

            return basePriceHotelSearchService.search(hotelSearchRequestDto);
        }

}

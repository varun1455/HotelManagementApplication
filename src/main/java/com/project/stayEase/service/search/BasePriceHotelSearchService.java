package com.project.stayEase.service.search;

import com.project.stayEase.dto.hotelSearchMappers.HotelSearchRequestDto;
import com.project.stayEase.dto.hotelSearchMappers.HotelSearchResponseDto;
import com.project.stayEase.dto.hotelSearchMappers.RoomSearchResponseDto;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.service.search.mapper.HotelSearchMapper;
import com.project.stayEase.service.search.assembler.RoomAvailabilityAssembler;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.service.search.projection.RoomAvailabilityProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasePriceHotelSearchService {


    private final InventoryRepository inventoryRepository;
    private final RoomAvailabilityAssembler roomAvailabilityAssembler;
    private final HotelSearchMapper hotelSearchMapper;


    public Page<HotelSearchResponseDto> search(HotelSearchRequestDto hotelSearchRequestDto){

        Pageable pageable = PageRequest.of(hotelSearchRequestDto.getPage(), hotelSearchRequestDto.getSize());

        LocalDate lastNight = hotelSearchRequestDto.getEndDate().minusDays(1);
        Long totalNights = ChronoUnit.DAYS.between(hotelSearchRequestDto.getStartDate(), hotelSearchRequestDto.getEndDate());

        Page<Hotel> hotelPage =  inventoryRepository.findHotelsWithAvailableInventory(hotelSearchRequestDto.getCity(), hotelSearchRequestDto.getStartDate(),
                lastNight, hotelSearchRequestDto.getRoomsCount(), totalNights, pageable);

        List<Long> hotelIds = hotelPage.getContent()
                .stream()
                .map(Hotel::getId)
                .toList();

        List<RoomAvailabilityProjection> roomAvailabilityProjections = inventoryRepository.findAvailableRooms(hotelIds,
                hotelSearchRequestDto.getStartDate(), lastNight,hotelSearchRequestDto.getRoomsCount(), totalNights);

        Map<Long, List<RoomSearchResponseDto>> availableRoomsInHotelMap = roomAvailabilityAssembler.availableRoomsInEachHotel(roomAvailabilityProjections);
        Map<Long, BigDecimal> mapOfStartingPrice = roomAvailabilityAssembler.getMinTotalPrice(roomAvailabilityProjections);

        List<HotelSearchResponseDto> hotelSearchResponse = hotelPage.getContent()
                .stream()
                .map(hotel-> hotelSearchMapper.toBaseResponse(hotel,
                        mapOfStartingPrice.getOrDefault(hotel.getId(), BigDecimal.ZERO),
                        availableRoomsInHotelMap.getOrDefault(hotel.getId(), Collections.emptyList())
                                )
                        ).toList();

        return new PageImpl<>(hotelSearchResponse, pageable, hotelPage.getTotalElements());
    }


}

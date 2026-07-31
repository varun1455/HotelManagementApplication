package com.project.stayEase.hotel;

import com.project.stayEase.dto.HotelSearchRequestDto;
import com.project.stayEase.dto.HotelSearchResponseDto;
import com.project.stayEase.dto.RoomSearchResponseDto;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.hotel.mapper.HotelSearchMapper;
import com.project.stayEase.hotel.assembler.RoomAvailabilityAssembler;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.hotel.projection.RoomAvailabilityProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class BasePriceHotelSearchService {


    private final InventoryRepository inventoryRepository;
    private final RoomAvailabilityAssembler roomAvailabilityAssembler;
    private final HotelSearchMapper hotelSearchMapper;


    public Page<HotelSearchResponseDto> search(HotelSearchRequestDto hotelSearchRequestDto){

        Pageable pageable = PageRequest.of(hotelSearchRequestDto.getPage(), hotelSearchRequestDto.getSize());

        Long totalDays = ChronoUnit.DAYS.between(hotelSearchRequestDto.getStartDate(), hotelSearchRequestDto.getEndDate());

        Page<Hotel> hotelPage =  inventoryRepository.findHotelsWithAvailableInventory(hotelSearchRequestDto.getCity(), hotelSearchRequestDto.getStartDate(),
                hotelSearchRequestDto.getEndDate(), hotelSearchRequestDto.getRoomsCount(), totalDays, pageable);

        List<Long> hotelIds = hotelPage.getContent()
                .stream()
                .map(Hotel::getId)
                .toList();

        List<RoomAvailabilityProjection> roomAvailabilityProjections = inventoryRepository.findAvailableRooms(hotelIds,
                hotelSearchRequestDto.getStartDate(), hotelSearchRequestDto.getEndDate(),hotelSearchRequestDto.getRoomsCount(), totalDays);

//        Map<Long, List<RoomSearchResponseDto>> availableRoomsInHotelMap  = roomAvailabilityProjections.stream()
//                .collect(Collectors.groupingBy(RoomAvailabilityProjection::getHotelId,
//                                Collectors.mapping(
//                                        projection-> {
//                                            RoomSearchResponseDto roomSearchResponseDto = modelMapper.map(projection.getRoom(), RoomSearchResponseDto.class);
//                                            roomSearchResponseDto.setAvailableNumberOfRooms(projection.getAvailableRooms());
//                                            roomSearchResponseDto.setTotalPrice(projection.getTotalPrice());
//                                            return roomSearchResponseDto;
//                                        },
//                                        Collectors.toList()
//                                )
//                        )
//                );
//        Map<Long, BigDecimal> mapOfStartingPrice =  roomAvailabilityProjections.stream()
//                .collect(Collectors.groupingBy(RoomAvailabilityProjection::getHotelId, Collectors.
//                                collectingAndThen(
//                                        Collectors.minBy(Comparator.comparing(
//                                                RoomAvailabilityProjection::getTotalPrice)
//                                        ), projection->
//                                                projection.map(RoomAvailabilityProjection::getTotalPrice).orElse(BigDecimal.ZERO)
//                                )
//                        )
//                );


        Map<Long, List<RoomSearchResponseDto>> availableRoomsInHotelMap = roomAvailabilityAssembler.availableRoomsInEachHotel(roomAvailabilityProjections);
        Map<Long, BigDecimal> mapOfStartingPrice = roomAvailabilityAssembler.getMinTotalPrice(roomAvailabilityProjections);


//        List<HotelSearchResponseDto> hotelSearchResponse = hotelPage.getContent()
//                .stream()
//                .map(hotel->{
//                    HotelSearchResponseDto dto =  modelMapper.map(hotel, HotelSearchResponseDto.class);
//                    dto.setRooms(availableRoomsInHotelMap.getOrDefault(hotel.getId(), Collections.emptyList()));
//                    dto.setStartingFrom(mapOfStartingPrice.getOrDefault(hotel.getId(), BigDecimal.ZERO));
//                    return dto;
//                })
//                .toList();


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

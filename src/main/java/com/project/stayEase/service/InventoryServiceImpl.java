package com.project.stayEase.service;


import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.*;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.Room;
import com.project.stayEase.repository.HotelMinPriceRepository;
import com.project.stayEase.repository.HotelRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.util.HotelSearchProjection;
import com.project.stayEase.util.RoomAvailabilityProjection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final HotelRepository hotelRepository;

    @Override
    public void initializeRoomForHalfYear(Room room) {
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusMonths(6);
            for(; !today.isAfter(endDate); today=today.plusDays(1) ) {
                Inventory inventory = Inventory.builder()
                        .hotel(room.getHotel())
                        .room(room)
                        .bookedCount(0)
                        .reservedCount(0)
                        .city(room.getHotel().getCity())
                        .date(today)
                        .price(room.getBasePrice())
                        .surgeFactor(BigDecimal.ONE)
                        .totalCount(room.getTotalCount())
                        .closed(false)
                        .build();

                inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventoriesForRoom(Room room) {
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<?> searchHotels(HotelSearchRequestDto hotelSearchRequestDto) {

        Pageable pageable = PageRequest.of(hotelSearchRequestDto.getPage(), hotelSearchRequestDto.getSize());

        Long totalDays = ChronoUnit.DAYS.between(hotelSearchRequestDto.getStartDate(), hotelSearchRequestDto.getEndDate()) + 1;

        int daysUntilCheckIn = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), hotelSearchRequestDto.getStartDate()));

        if(daysUntilCheckIn<=60){
            Page<HotelSearchProjection>  projections =  hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequestDto.getCity(), hotelSearchRequestDto.getStartDate(),
                    hotelSearchRequestDto.getEndDate(), pageable);

            Map<Long, BigDecimal> starttingPriceMap = projections.getContent().stream()
                     .collect(Collectors.toMap(
                             HotelSearchProjection::hotelId,
                             HotelSearchProjection::startingFrom
                     )
            );

            List<Long> hotelIds = projections.getContent()
                            .stream()
                            .map(HotelSearchProjection::hotelId)
                            .toList();

            List<Hotel> hotels = hotelRepository.findAllById(hotelIds);

            List<HotelMinPriceSearchResponseDto> hotelMinPriceSearchResponse = hotels.stream()
                    .map(hotel-> {
                        HotelMinPriceSearchResponseDto dto = modelMapper.map(hotel, HotelMinPriceSearchResponseDto.class);
                        dto.setStartingFrom(starttingPriceMap.get(hotel.getId()));
                        return dto;

                    }).toList();

            return new PageImpl<>(hotelMinPriceSearchResponse, pageable, projections.getTotalElements());
        }

        Page<Hotel> hotelPage =  inventoryRepository.findHotelsWithAvailableInventory(hotelSearchRequestDto.getCity(), hotelSearchRequestDto.getStartDate(),
                hotelSearchRequestDto.getEndDate(), hotelSearchRequestDto.getRoomsCount(), totalDays, pageable);

        List<Long> hotelIds = hotelPage.getContent()
                .stream()
                .map(Hotel::getId)
                .toList();

        System.out.println(hotelIds);
        List<RoomAvailabilityProjection> roomAvailabilityProjections = inventoryRepository.findAvailableRooms(hotelIds,
                hotelSearchRequestDto.getStartDate(), hotelSearchRequestDto.getEndDate(), totalDays);

        Map<Long, List<RoomSearchResponseDto>> availableRoomsInHotelMap  = roomAvailabilityProjections.stream()
                .collect(Collectors.groupingBy(RoomAvailabilityProjection::getHotelId,
                            Collectors.mapping(
                                    projection-> {
                                        RoomSearchResponseDto roomSearchResponseDto = modelMapper.map(projection.getRoom(), RoomSearchResponseDto.class);
                                        roomSearchResponseDto.setAvailableNumberOfRooms(projection.getAvailableRooms());
                                        roomSearchResponseDto.setTotalPrice(projection.getTotalPrice());
                                        return roomSearchResponseDto;
                                    },
                                    Collectors.toList()
                            )
                )
        );
        Map<Long, BigDecimal> mapOfStartingPrice =  roomAvailabilityProjections.stream()
                .collect(Collectors.groupingBy(RoomAvailabilityProjection::getHotelId, Collectors.
                        collectingAndThen(
                            Collectors.minBy(Comparator.comparing(
                                    RoomAvailabilityProjection::getTotalPrice)
                            ), projection->
                                projection.map(RoomAvailabilityProjection::getTotalPrice).orElse(BigDecimal.ZERO)
                        )
                )
        );


        List<HotelSearchResponseDto> hotelSearchResponse = hotelPage.getContent()
                .stream()
                .map(hotel->{
                   HotelSearchResponseDto dto =  modelMapper.map(hotel, HotelSearchResponseDto.class);
                   dto.setRooms(availableRoomsInHotelMap.getOrDefault(hotel.getId(), Collections.emptyList()));
                   dto.setStartingFrom(mapOfStartingPrice.getOrDefault(hotel.getId(), BigDecimal.ZERO));
                    return dto;
                })
                .toList();

        return new PageImpl<>(hotelSearchResponse, pageable, hotelPage.getTotalElements());

    }


}

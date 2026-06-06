package com.project.stayEase.service;


import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.*;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.Room;
import com.project.stayEase.repository.HotelMinPriceRepository;
import com.project.stayEase.repository.HotelRepository;
import com.project.stayEase.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

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

        if(daysUntilCheckIn<=90){
            return hotelMinPriceRepository.findHotelsWithAvailableInventory(hotelSearchRequestDto.getCity(), hotelSearchRequestDto.getStartDate(),
                    hotelSearchRequestDto.getEndDate(), hotelSearchRequestDto.getRoomsCount(), totalDays, pageable);
        }

        Page<Hotel> hotelPage =  inventoryRepository.findHotelsWithAvailableInventory(hotelSearchRequestDto.getCity(), hotelSearchRequestDto.getStartDate(),
                hotelSearchRequestDto.getEndDate(), hotelSearchRequestDto.getRoomsCount(), totalDays, pageable);
        return hotelPage.map((element) -> modelMapper.map(element, HotelResponseDto.class));

    }


}

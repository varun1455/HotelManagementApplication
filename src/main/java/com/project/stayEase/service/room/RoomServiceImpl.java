package com.project.stayEase.service.room;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.room.request.RoomRequestDto;
import com.project.stayEase.dto.room.response.RoomResponseDto;
import com.project.stayEase.dto.bedType.response.RoomUpdateResult;
import com.project.stayEase.entity.BedType;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Room;
import com.project.stayEase.entity.RoomType;
import com.project.stayEase.repository.*;
import com.project.stayEase.service.inventory.InventoryService;
import com.project.stayEase.security.utils.SecurityUtils;
import com.project.stayEase.service.pricing.update.PricingWindowUpdateService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final BedTypeRepository bedTypeRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;
    private final PricingWindowUpdateService pricingWindowUpdateService;
    private final RoomUpdateService roomUpdateService;

    @Override
    @Transactional
    public RoomResponseDto createRoom(Long hotelId, RoomRequestDto roomRequestDto) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + hotelId));
        securityUtils.validateHotelOwnership(hotel);
        RoomType roomType = roomTypeRepository.findById(roomRequestDto.getRoomTypeId()).orElseThrow(()->new ResourceNotFoundException("RoomType not found " + roomRequestDto.getRoomTypeId()));
        BedType bedType = bedTypeRepository.findById(roomRequestDto.getBedTypeId()).orElseThrow(()->new ResourceNotFoundException("BedType not found " + roomRequestDto.getBedTypeId()));
        Room room = modelMapper.map(roomRequestDto, Room.class);
        room.setType(roomType);
        room.setBedType(bedType);
        room.setHotel(hotel);
        roomRepository.save(room);

        if(hotel.isActive()){
            inventoryService.initializeRoom(room);
            pricingWindowUpdateService.initializeRollingWindowPricing(room);
        }
        return modelMapper.map(room, RoomResponseDto.class);
    }

    @Override
    public List<RoomResponseDto> getAllRoomsInHotel(Long hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + hotelId));
        securityUtils.validateHotelOwnership(hotel);
        return roomRepository.findByHotelId(hotelId)
                .stream()
                .map(room -> modelMapper.map(room, RoomResponseDto.class))
                .toList();
    }

    @Override
    public RoomResponseDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found with id " + roomId));
        securityUtils.validateHotelOwnership(room.getHotel());
        return modelMapper.map(room, RoomResponseDto.class);
    }

    @Transactional
    public RoomResponseDto updateRoom(Long roomId, RoomRequestDto request) {

        Room room = roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found with id " + roomId));

        securityUtils.validateHotelOwnership(room.getHotel());

        RoomUpdateResult result = roomUpdateService.update(room, request);

        roomRepository.save(room);

        if (room.getHotel().isActive() && result.requiresPricingUpdate()) {
            inventoryService.updateFutureInventory(room, room.getBasePrice(), room.getTotalCount());

            pricingWindowUpdateService.initializeRollingWindowPricing(room);
        }

        return modelMapper.map(room, RoomResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found with id " + roomId));
        securityUtils.validateHotelOwnership(room.getHotel());
        inventoryService.deleteInventories(room);
        roomRepository.deleteById(roomId);
    }

    @Override
    public Room getRoomOfHotel(Long id) {
         return roomRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("Room not found with id " + id));

    }
}

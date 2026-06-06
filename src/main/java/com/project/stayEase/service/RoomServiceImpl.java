package com.project.stayEase.service;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.RoomRequestDto;
import com.project.stayEase.dto.RoomResponseDto;
import com.project.stayEase.entity.BedType;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Room;
import com.project.stayEase.entity.RoomType;
import com.project.stayEase.repository.BedTypeRepository;
import com.project.stayEase.repository.HotelRepository;
import com.project.stayEase.repository.RoomRepository;
import com.project.stayEase.repository.RoomTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    @Override
    @Transactional
    public RoomResponseDto createRoom(Long hotelId, RoomRequestDto roomRequestDto) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + hotelId));
        RoomType roomType = roomTypeRepository.findById(roomRequestDto.getRoomTypeId()).orElseThrow(()->new ResourceNotFoundException("RoomType not found " + roomRequestDto.getRoomTypeId()));
        BedType bedType = bedTypeRepository.findById(roomRequestDto.getBedTypeId()).orElseThrow(()->new ResourceNotFoundException("BedType not found " + roomRequestDto.getBedTypeId()));
        Room room = modelMapper.map(roomRequestDto, Room.class);
        room.setType(roomType);
        room.setBedType(bedType);
        room.setHotel(hotel);
        roomRepository.save(room);

        if(hotel.isActive()){
            inventoryService.initializeRoomForHalfYear(room);
        }
        return modelMapper.map(room, RoomResponseDto.class);
    }

    @Override
    public List<RoomResponseDto> getAllRoomsInHotel(Long hotelId) {
        if(!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found with id " + hotelId);
        }
        return roomRepository.findByHotelId(hotelId)
                .stream()
                .map(room -> modelMapper.map(room, RoomResponseDto.class))
                .toList();
    }

    @Override
    public RoomResponseDto getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found with id " + roomId));
        return modelMapper.map(room, RoomResponseDto.class);
    }

    @Override
    public RoomResponseDto updateRoom(Long roomId, RoomRequestDto roomRequestDto) {
        if(!roomRepository.existsById(roomId)) throw new ResourceNotFoundException("Hotel not found with id " + roomId);
        roomTypeRepository.findById(roomRequestDto.getRoomTypeId()).orElseThrow(()->new ResourceNotFoundException("RoomType not found " + roomRequestDto.getRoomTypeId()));
        bedTypeRepository.findById(roomRequestDto.getBedTypeId()).orElseThrow(()->new ResourceNotFoundException("BedType not found " + roomRequestDto.getBedTypeId()));
        Room updatedRoom = modelMapper.map(roomRequestDto, Room.class);
        roomRepository.save(updatedRoom);
        return modelMapper.map(updatedRoom, RoomResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found with id " + roomId));
        inventoryService.deleteAllInventoriesForRoom(room);
        roomRepository.deleteById(roomId);
    }
}

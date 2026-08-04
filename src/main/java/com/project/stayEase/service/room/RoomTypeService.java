package com.project.stayEase.service.room;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.roomMappers.RoomTypeRequestDto;
import com.project.stayEase.dto.roomMappers.RoomTypeResponseDto;
import com.project.stayEase.entity.RoomType;
import com.project.stayEase.repository.RoomTypeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomTypeService  {

    private final RoomTypeRepository roomTypeRepository;
    private final ModelMapper modelMapper;

    public RoomTypeResponseDto createRoomType(RoomTypeRequestDto roomTypeRequestDto) {
        RoomType roomType = modelMapper.map(roomTypeRequestDto, RoomType.class);
        roomTypeRepository.save(roomType);
        return modelMapper.map(roomType, RoomTypeResponseDto.class);
    }

    public RoomTypeResponseDto findRoomType(Long roomTypeId) {
       RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(()-> new ResourceNotFoundException("RoomType not exist with this id " + roomTypeId));
       return modelMapper.map(roomType, RoomTypeResponseDto.class);
    }

    public List<RoomTypeResponseDto> findAllRoomTypes() {
        return roomTypeRepository.findAll()
                .stream()
                .map(roomtype-> modelMapper.map(roomtype, RoomTypeResponseDto.class))
                .toList();
    }
}

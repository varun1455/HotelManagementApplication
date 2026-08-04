package com.project.stayEase.service.room;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.roomMappers.BedTypeRequestDto;
import com.project.stayEase.dto.roomMappers.BedTypeResponseDto;
import com.project.stayEase.entity.BedType;
import com.project.stayEase.repository.BedTypeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BedTypeService {

    private final BedTypeRepository bedTypeRepository;
    private final ModelMapper modelMapper;


    public BedTypeResponseDto createBedType(BedTypeRequestDto bedTypeRequestDto) {
        BedType bedType = modelMapper.map(bedTypeRequestDto, BedType.class);
        bedTypeRepository.save(bedType);
        return modelMapper.map(bedType, BedTypeResponseDto.class);
    }


    public BedTypeResponseDto findBedTypeById(long bedTypeId) {
        BedType bedType = bedTypeRepository.findById(bedTypeId).orElseThrow(()-> new ResourceNotFoundException("Bed type does not exist with id "+ bedTypeId));
        return modelMapper.map(bedType, BedTypeResponseDto.class);
    }


    public List<BedTypeResponseDto> findAllBedTypes() {
        return bedTypeRepository.findAll()
                .stream()
                .map(bedType-> modelMapper.map(bedType, BedTypeResponseDto.class))
                .toList();
    }
}

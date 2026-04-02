package com.project.stayEase.service;

import com.project.stayEase.dto.BedTypeRequestDto;
import com.project.stayEase.dto.BedTypeResponseDto;

import java.util.List;

public interface BedTypeService {

    BedTypeResponseDto createBedType(BedTypeRequestDto bedTypeRequestDto);

    BedTypeResponseDto findBedTypeById(long bedTypeId);
    List<BedTypeResponseDto> findAllBedTypes();
}

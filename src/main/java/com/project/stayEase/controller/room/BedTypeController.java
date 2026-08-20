package com.project.stayEase.controller.room;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.roomMappers.BedTypeRequestDto;
import com.project.stayEase.dto.roomMappers.BedTypeResponseDto;
import com.project.stayEase.service.room.BedTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/bedType")
public class BedTypeController {

    private final BedTypeService bedTypeService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<BedTypeResponseDto>> createBedType(@RequestBody BedTypeRequestDto bedTypeRequestDto) {
        BedTypeResponseDto bedTypeResponseDto = bedTypeService.createBedType(bedTypeRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/{bedtypeId}")
    @PreAuthorize("hasAnyRole('HOTEL_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<BedTypeResponseDto>> getBedType(@PathVariable Long bedtypeId) {
        BedTypeResponseDto bedTypeResponseDto = bedTypeService.findBedTypeById(bedtypeId);
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDto), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('HOTEL_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<List<BedTypeResponseDto>>> getAllBedTypes() {
        List<BedTypeResponseDto> bedTypeResponseDtos = bedTypeService.findAllBedTypes();
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDtos), HttpStatus.OK);
    }

}

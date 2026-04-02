package com.project.stayEase.controller;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.BedTypeRequestDto;
import com.project.stayEase.dto.BedTypeResponseDto;
import com.project.stayEase.service.BedTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/bedType")
public class BedTypeController {

    private final BedTypeService bedTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<BedTypeResponseDto>> createRoomType(@RequestBody BedTypeRequestDto bedTypeRequestDto) {
        BedTypeResponseDto bedTypeResponseDto = bedTypeService.createBedType(bedTypeRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/{bedtypeId}")
    public ResponseEntity<ApiResponse<BedTypeResponseDto>> getRoomType(@PathVariable Long bedtypeId) {
        BedTypeResponseDto bedTypeResponseDto = bedTypeService.findBedTypeById(bedtypeId);
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDto), HttpStatus.OK);
    }

    @GetMapping("/allTypes")
    public ResponseEntity<ApiResponse<List<BedTypeResponseDto>>> getAllRoomTypes() {
        List<BedTypeResponseDto> bedTypeResponseDtos = bedTypeService.findAllBedTypes();
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDtos), HttpStatus.OK);
    }

}

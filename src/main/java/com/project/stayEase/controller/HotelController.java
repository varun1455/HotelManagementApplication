package com.project.stayEase.controller;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.HotelRequestDto;
import com.project.stayEase.dto.HotelResponseDto;
import com.project.stayEase.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<ApiResponse<HotelResponseDto>> createHotel(@RequestBody HotelRequestDto hotelRequestDto) {
        log.info("attempting hotel with hotelRequestDto={}", hotelRequestDto);
        HotelResponseDto hotelResponseDto = hotelService.createNewHotel(hotelRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(hotelResponseDto),HttpStatus.CREATED);

    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelResponseDto>> getHotelById(@PathVariable Long hotelId) {
        log.info("attempting hotel with hotelId={}", hotelId);
        HotelResponseDto hotelResponseDto = hotelService.getHotelById(hotelId);
        return new ResponseEntity<>(ApiResponse.successResponse(hotelResponseDto),HttpStatus.OK);

    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<HotelResponseDto>> updateHotelById(@PathVariable Long hotelId, @RequestBody HotelRequestDto hotelRequestDto) {
        log.info("attempting hotel with hotelId={}", hotelId);
        HotelResponseDto hotelResponseDto =  hotelService.updateHotelById(hotelId, hotelRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(hotelResponseDto),HttpStatus.OK);
    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<Void> activateHotel(@PathVariable Long hotelId) {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long hotelId){
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }
}

package com.project.stayEase.controller;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.HotelInfoDto;
import com.project.stayEase.dto.HotelResponseDto;
import com.project.stayEase.dto.HotelSearchRequestDto;
import com.project.stayEase.service.HotelService;
import com.project.stayEase.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<?>>> searchHotels(@RequestBody HotelSearchRequestDto hotelSearchRequestDto){

        Page<?> page = inventoryService.searchHotels(hotelSearchRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(page), HttpStatus.OK);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<ApiResponse<HotelInfoDto>> getHotelInfo(@PathVariable Long hotelId){
        HotelInfoDto hotelInfoDto = hotelService.findHotelInfo(hotelId);
        return new ResponseEntity<>(ApiResponse.successResponse(hotelInfoDto), HttpStatus.OK);
    }
}

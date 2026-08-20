package com.project.stayEase.controller.search;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.hotelMappers.HotelInfoDto;
import com.project.stayEase.dto.hotelSearchMappers.HotelSearchRequestDto;
import com.project.stayEase.dto.hotelSearchMappers.HotelSearchResponseDto;
import com.project.stayEase.service.search.HotelSearchService;
import com.project.stayEase.service.hotel.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelSearchController {

    private final HotelSearchService hotelSearchService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<HotelSearchResponseDto>>> searchHotels(@Valid @RequestBody HotelSearchRequestDto hotelSearchRequestDto){

        Page<HotelSearchResponseDto> page = hotelSearchService.search(hotelSearchRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(page), HttpStatus.OK);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<ApiResponse<HotelInfoDto>> getHotelInfo(@PathVariable Long hotelId){
        HotelInfoDto hotelInfoDto = hotelService.findHotelInfo(hotelId);
        return new ResponseEntity<>(ApiResponse.successResponse(hotelInfoDto), HttpStatus.OK);
    }
}

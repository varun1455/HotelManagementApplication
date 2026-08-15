package com.project.stayEase.controller.hotel;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.bookingMappers.QueryBookingsDto;
import com.project.stayEase.dto.hotelMappers.HotelRequestDto;
import com.project.stayEase.dto.hotelMappers.HotelResponseDto;
import com.project.stayEase.service.booking.services.query.BookingQueryService;
import com.project.stayEase.service.hotel.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;
    private final BookingQueryService bookingQueryService;

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

    @GetMapping
    public ResponseEntity<ApiResponse<List<HotelResponseDto>>> getAllHotels(){
        return new ResponseEntity<>(ApiResponse.successResponse(hotelService.getAllHotelsOfOwner()), HttpStatus.OK);
    }

    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<ApiResponse<List<QueryBookingsDto>>> getAllBookingsOfHotel(@PathVariable Long hotelId){
            return new ResponseEntity<>(ApiResponse.successResponse(bookingQueryService.getAllBookingsByHotelId(hotelId)), HttpStatus.OK) ;

    }
}

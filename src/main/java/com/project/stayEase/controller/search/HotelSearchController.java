package com.project.stayEase.controller.search;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.search.response.HotelInfoDto;
import com.project.stayEase.dto.search.request.HotelSearchRequestDto;
import com.project.stayEase.dto.search.response.HotelSearchResponseDto;
import com.project.stayEase.service.search.HotelSearchService;
import com.project.stayEase.service.hotel.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Tag(
        name = "Hotel Search",
        description = "Search and retrieve hotels"
)
public class HotelSearchController {

    private final HotelSearchService hotelSearchService;
    private final HotelService hotelService;

    @Operation(
            summary = "Search hotels",
            description = """
                     Searches for available hotels based on location and stay dates.
                    
                     Check-in date must be today or a future date.
                     Check-out date must be after the check-in date.
                    """
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<HotelSearchResponseDto>>> searchHotels(@Valid HotelSearchRequestDto hotelSearchRequestDto){

        Page<HotelSearchResponseDto> page = hotelSearchService.search(hotelSearchRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(page), HttpStatus.OK);
    }

    @Operation(
            summary = "Get hotel information",
            description = "Returns detailed information about a specific hotel."
    )
    @GetMapping("/{hotelId}/info")
    public ResponseEntity<ApiResponse<HotelInfoDto>> getHotelInfo(@PathVariable Long hotelId){
        HotelInfoDto hotelInfoDto = hotelService.findHotelInfo(hotelId);
        return new ResponseEntity<>(ApiResponse.successResponse(hotelInfoDto), HttpStatus.OK);
    }
}

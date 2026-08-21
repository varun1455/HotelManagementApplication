package com.project.stayEase.controller.holiday;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.service.holiday.services.PublicHolidaysService;
import com.project.stayEase.dto.holiday.response.CalendarificResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/holidays")
@RequiredArgsConstructor
@Tag(
        name = "Holiday Management",
        description = "Holiday synchronization and holiday pricing rules"
)
@SecurityRequirement(name = "bearerAuth")
public class PublicHolidaysController {

    private final PublicHolidaysService publicHolidaysService;

    @Operation(
            summary = "Synchronize public holidays",
            description = """
                    Fetches and synchronizes public holiday information
                    for the specified year.

                    The synchronized holidays are used by the hotel's
                    dynamic pricing system.
                    """
    )
    @PostMapping("/{year}/sync")
    public ResponseEntity<ApiResponse<CalendarificResponse>> syncHolidays(
            @Parameter(
                    description = "Year for which holidays should be synchronized",
                    example = "2026"
            )@PathVariable int year){
        CalendarificResponse response = publicHolidaysService.syncHolidays(year);
        return new ResponseEntity<>(ApiResponse.successResponse(response), HttpStatusCode.valueOf(response.meta().code()));

    }

}

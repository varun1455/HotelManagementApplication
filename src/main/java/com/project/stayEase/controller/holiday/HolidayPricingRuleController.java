package com.project.stayEase.controller.holiday;

import com.project.stayEase.dto.holiday.request.UpdatePriceFactorDto;
import com.project.stayEase.service.holiday.services.HolidayPricingRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/system/pricingrule")
@Tag(
        name = "Holiday Management",
        description = "Holiday synchronization and holiday pricing rules"
)
@SecurityRequirement(name = "bearerAuth")
public class HolidayPricingRuleController {

    private final HolidayPricingRuleService holidayPricingRuleService;

    @Operation(
            summary = "Update holiday pricing factor",
            description = """
                    Updates the dynamic pricing factor associated with
                    a specific holiday type.

                    The factor is used when calculating room prices
                    for inventories affected by that holiday type.
                    """
    )
    @PatchMapping("/holidayType/{id}")
    public ResponseEntity<Void> updatePricingFactorForHolidayType(
            @Parameter(
                    description = "ID of the holiday type",
                    example = "1"
            )@PathVariable Long id,
            @RequestBody UpdatePriceFactorDto priceFactorDto){
            holidayPricingRuleService.updatePricingFactorOFHolidayType(id, priceFactorDto);
        return ResponseEntity.noContent().build();

    }
}

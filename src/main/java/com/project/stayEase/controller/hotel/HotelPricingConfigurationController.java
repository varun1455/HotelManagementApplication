package com.project.stayEase.controller.hotel;

import com.project.stayEase.dto.hotel.request.HotelPricingConfigurationDto;
import com.project.stayEase.service.pricing.configuration.HotelPricingConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(
        name = "Hotel Pricing Configuration",
        description = "Hotel managers Api to configure dynamic pricing rules for their hotels"
)
public class HotelPricingConfigurationController {

    private final HotelPricingConfigurationService hotelPricingConfigurationService;

    @Operation(
            summary = "Update hotel pricing configuration",
            description = """
                    Updates the dynamic pricing configuration for the currently
                    authenticated hotel manager.

                    The configuration controls surge pricing, urgency pricing
                    and occupancy-based pricing.
                    """
    )
    @PatchMapping("/priceConfig")
    public ResponseEntity<Void> updateHotelPricingConfiguration(@RequestBody HotelPricingConfigurationDto dto){

        hotelPricingConfigurationService.updateHotelPricingConfiguration(dto);
        return ResponseEntity.noContent().build();
    }


}

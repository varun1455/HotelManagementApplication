package com.project.stayEase.controller.room;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.bedType.request.BedTypeRequestDto;
import com.project.stayEase.dto.bedType.response.BedTypeResponseDto;
import com.project.stayEase.service.room.BedTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/bedType")
@Tag(
        name = "Bed Type Management",
        description = "System-defined bed types"
)
public class BedTypeController {

    private final BedTypeService bedTypeService;

    @Operation(
            summary = "Create a bed type",
            description = "Creates a new system-level bed type. Only system administrators can perform this operation."
    )
    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<BedTypeResponseDto>> createBedType(@RequestBody BedTypeRequestDto bedTypeRequestDto) {
        BedTypeResponseDto bedTypeResponseDto = bedTypeService.createBedType(bedTypeRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get bed type by ID",
            description = "Returns a system-defined bed type."
    )
    @GetMapping("/{bedtypeId}")
    @PreAuthorize("hasAnyRole('HOTEL_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<BedTypeResponseDto>> getBedType(@PathVariable Long bedtypeId) {
        BedTypeResponseDto bedTypeResponseDto = bedTypeService.findBedTypeById(bedtypeId);
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all bed types",
            description = "Returns all available system-defined bed types."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('HOTEL_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<List<BedTypeResponseDto>>> getAllBedTypes() {
        List<BedTypeResponseDto> bedTypeResponseDtos = bedTypeService.findAllBedTypes();
        return new ResponseEntity<>(ApiResponse.successResponse(bedTypeResponseDtos), HttpStatus.OK);
    }

}

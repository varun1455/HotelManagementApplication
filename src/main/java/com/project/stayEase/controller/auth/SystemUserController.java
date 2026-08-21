package com.project.stayEase.controller.auth;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.auth.request.SignUpRequestDto;
import com.project.stayEase.dto.auth.response.UserResponseDto;
import com.project.stayEase.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/users")
@Tag(
        name = "System User Management",
        description = "System administrator APIs for managing system administrators and hotel managers"
)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SystemUserController {

    private final AuthService authService;

    @Operation(
            summary = "Create hotel manager",
            description = "Creates a new hotel manager account. "
                    + "Only an authenticated system administrator can perform this operation."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Hotel manager created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid user data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Only system administrators can create hotel managers"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "User with the given email already exists"
            )
    })
    @PostMapping("/hotel-manager")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> createHotelManager(@RequestBody SignUpRequestDto request) {

        UserResponseDto response = authService.createHotelManager(request);

        return new ResponseEntity<>(ApiResponse.successResponse(response), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Create system administrator",
            description = "Creates a new system administrator account. "
                    + "Only an authenticated system administrator can perform this operation."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "System administrator created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid user data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Only system administrators can create system administrators"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "User with the given email already exists"
            )
    })

    @PostMapping("/system-admin")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> createSystemAdmin(@RequestBody SignUpRequestDto request) {

        UserResponseDto response = authService.createSystemAdmin(request);

        return new ResponseEntity<>(ApiResponse.successResponse(response), HttpStatus.CREATED);
    }
}

package com.project.stayEase.controller.authandUsers;


import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.authMappers.SignUpRequestDto;
import com.project.stayEase.dto.authMappers.UserResponseDto;
import com.project.stayEase.service.auth.AuthService;
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
@RequiredArgsConstructor
public class SystemUserController {

    private final AuthService authService;

    @PostMapping("/hotel-manager")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> createHotelManager(@RequestBody SignUpRequestDto request) {

        UserResponseDto response = authService.createHotelManager(request);

        return new ResponseEntity<>(ApiResponse.successResponse(response), HttpStatus.CREATED);
    }

    @PostMapping("/system-admin")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> createSystemAdmin(@RequestBody SignUpRequestDto request) {

        UserResponseDto response = authService.createSystemAdmin(request);

        return new ResponseEntity<>(ApiResponse.successResponse(response), HttpStatus.CREATED);
    }
}

package com.project.stayEase.controller.authandUsers;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.authMappers.*;
import com.project.stayEase.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.refreshTokenExpiration}")
    private int cookieMaxAge;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        return new ResponseEntity<>(ApiResponse.successResponse(authService.signup(signUpRequestDto)), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        AuthResponseDto authResponseDto = authService.login(loginRequestDto);
        Cookie cookie = new Cookie("refreshToken", authResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieMaxAge);
        httpServletResponse.addCookie(cookie);
        return new ResponseEntity<>(ApiResponse.successResponse(new LoginResponseDto(authResponseDto.getAccessToken())), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refreshToken(HttpServletRequest request) {

        String accessToken = authService.refreshAccessToken(request);

        AuthResponseDto response = new AuthResponseDto(accessToken, null);

        return ResponseEntity.ok(ApiResponse.successResponse(response)
        );
    }



}

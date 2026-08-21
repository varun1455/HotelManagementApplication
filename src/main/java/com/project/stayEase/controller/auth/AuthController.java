package com.project.stayEase.controller.auth;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.auth.request.LoginRequestDto;
import com.project.stayEase.dto.auth.request.SignUpRequestDto;
import com.project.stayEase.dto.auth.response.AuthResponseDto;
import com.project.stayEase.dto.auth.response.LoginResponseDto;
import com.project.stayEase.dto.auth.response.UserResponseDto;
import com.project.stayEase.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Authentication",
        description = "User registration, login and JWT token management"
)
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.refreshTokenExpiration}")
    private int cookieMaxAge;

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new guest account"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid signup request"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "User already exists"
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        return new ResponseEntity<>(ApiResponse.successResponse(authService.signup(signUpRequestDto)), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns a JWT access token. "
                    + "The refresh token is stored in an HttpOnly cookie."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid email or password"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse){

        AuthResponseDto authResponseDto = authService.login(loginRequestDto);
        Cookie cookie = new Cookie("refreshToken", authResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieMaxAge);
        httpServletResponse.addCookie(cookie);
        return new ResponseEntity<>(ApiResponse.successResponse(new LoginResponseDto(authResponseDto.getAccessToken())), HttpStatus.OK);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new JWT access token using the refresh token stored in the HttpOnly cookie."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Access token refreshed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Refresh token is missing, expired or invalid"
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refreshToken(HttpServletRequest request) {

        String accessToken = authService.refreshAccessToken(request);

        AuthResponseDto response = new AuthResponseDto(accessToken, null);

        return ResponseEntity.ok(ApiResponse.successResponse(response)
        );
    }



}

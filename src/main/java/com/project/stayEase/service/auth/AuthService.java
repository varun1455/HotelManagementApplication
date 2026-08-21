package com.project.stayEase.service.auth;

import com.project.stayEase.dto.auth.response.AuthResponseDto;
import com.project.stayEase.dto.auth.request.LoginRequestDto;
import com.project.stayEase.dto.auth.request.SignUpRequestDto;
import com.project.stayEase.dto.auth.response.UserResponseDto;
import com.project.stayEase.entity.User;
import com.project.stayEase.entity.enums.Role;
import com.project.stayEase.repository.UserRepository;
import com.project.stayEase.service.pricing.configuration.HotelPricingConfigurationService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final HotelPricingConfigurationService hotelPricingConfigurationService;
    private final JwtService jwtService;

    @Transactional
    public UserResponseDto signup(SignUpRequestDto signUpRequestDto) {

        validateEmail(signUpRequestDto.getEmail());

        User newUser = buildUser(signUpRequestDto, Role.GUEST);

        User savedUser = userRepository.save(newUser);

        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto createHotelManager(SignUpRequestDto request) {

        validateEmail(request.getEmail());

        User user = buildUser(request, Role.HOTEL_MANAGER);

        User savedUser = userRepository.save(user);

        hotelPricingConfigurationService.initializeDefaultHotelPriceConfigurationForHotelManager(savedUser);

        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Transactional
    public UserResponseDto createSystemAdmin(SignUpRequestDto request){

        validateEmail(request.getEmail());

        User user = buildUser(request, Role.SYSTEM_ADMIN);

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    private User buildUser(SignUpRequestDto request, Role role) {

        User user = modelMapper.map(request, User.class);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRoles(Set.of(role));

        return user;
    }

    private void validateEmail(String email){
        if (userRepository.existsByEmail(email)){
            throw new RuntimeException("User already exists with same email");
        }
    }

    public AuthResponseDto login(LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword()
        ));

        User user = (User) authentication.getPrincipal();

        assert user != null;
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponseDto(accessToken, refreshToken);
    }

    public String refreshAccessToken(HttpServletRequest request){
            String refreshToken = jwtService.getRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            throw new BadCredentialsException("Refresh token not found");
        }
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }
            Long userId = jwtService.getUserIdFromToken(refreshToken);
            User user = userRepository.findById(userId).orElseThrow(()-> new JwtException("User not found"));
        return jwtService.generateAccessToken(user);
    }
}

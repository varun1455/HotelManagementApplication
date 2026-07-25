package com.project.stayEase.security;

import com.project.stayEase.dto.AuthResponseDto;
import com.project.stayEase.dto.LoginRequestDto;
import com.project.stayEase.dto.SignUpRequestDto;
import com.project.stayEase.dto.UserResponseDto;
import com.project.stayEase.entity.User;
import com.project.stayEase.entity.enums.Role;
import com.project.stayEase.repository.UserRepository;
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

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserResponseDto signup(SignUpRequestDto signUpRequestDto){

        User user = userRepository.findByEmail(signUpRequestDto.getEmail()).orElse(null);
        if(user!=null){
            throw new RuntimeException("user already exists with same email");
        }

        User newUser = modelMapper.map(signUpRequestDto, User.class);
        newUser.setRoles(Set.of(Role.GUEST, Role.HOTEL_MANAGER, Role.SYSTEM_ADMIN));
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

        userRepository.save(newUser);

        return modelMapper.map(newUser, UserResponseDto.class);

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

    public String getRefreshToken(HttpServletRequest request){
            String refreshToken = jwtService.getRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            throw new BadCredentialsException(
                    "Refresh token not found"
            );
        }
            Long userId = jwtService.getUserIdFromToken(refreshToken);
            User user = userRepository.findById(userId).orElseThrow(()-> new JwtException("User not found"));
        return jwtService.generateAccessToken(user);
    }
}

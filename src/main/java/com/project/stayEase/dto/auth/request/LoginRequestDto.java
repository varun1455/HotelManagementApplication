package com.project.stayEase.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequestDto {

    @Schema(
            description = "User's registered email address",
            example = "manager1@gmail.com"
    )
    private String email;

    @Schema(
            description = "User account password",
            example = "Manager@12345",
            format = "password"
    )
    private String password;
}

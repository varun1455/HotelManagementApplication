package com.project.stayEase.dto.auth.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignUpRequestDto {

    @Schema(
            description = "User's email address",
            example = "john@gmail.com"
    )
    private String name;

    @Schema(
            description = "User's password",
            example = "StrongPassword@123",
            format = "password"
    )
    private String email;

    @Schema(
            description = "User's display name",
            example = "John"
    )
    private String password;

}

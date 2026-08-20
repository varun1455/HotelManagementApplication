package com.project.stayEase.dto.authMappers;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String email;
    private String password;
}

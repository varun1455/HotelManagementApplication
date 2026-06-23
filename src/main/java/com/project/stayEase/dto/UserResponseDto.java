package com.project.stayEase.dto;

import com.project.stayEase.entity.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDto {

    private Long id;
    private String email;
    private String name;
}

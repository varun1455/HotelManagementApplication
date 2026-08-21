package com.project.stayEase.dto.guests.response;

import com.project.stayEase.entity.enums.Gender;
import lombok.Data;

@Data
public class GuestResponseDto {

    private Long id;
    private String name;
    private Gender gender;
    private Integer age;
}

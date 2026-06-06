package com.project.stayEase.dto;

import com.project.stayEase.entity.enums.Gender;
import lombok.Data;


@Data
public class GuestRequestDto {

    private String name;
    private Gender gender;
    private Integer age;
}

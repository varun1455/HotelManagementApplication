package com.project.stayEase.dto.guests.request;

import com.project.stayEase.entity.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "Guest information associated with a booking")
public class GuestRequestDto {

    @Schema(
            description = "Full name of the guest",
            example = "John Doe"
    )
    private String name;

    @Schema(
            description = "Gender of the guest",
            example = "MALE"
    )
    private Gender gender;

    @Schema(
            description = "Age of the guest",
            example = "32"
    )
    private Integer age;
}

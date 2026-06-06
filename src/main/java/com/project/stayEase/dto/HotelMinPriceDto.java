package com.project.stayEase.dto;

import com.project.stayEase.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelMinPriceDto {
    private Hotel hotel;
    private BigDecimal price;
}

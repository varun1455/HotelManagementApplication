package com.project.stayEase.dto.checkout;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSessionDto {
    
    private String sessionUrl;
}

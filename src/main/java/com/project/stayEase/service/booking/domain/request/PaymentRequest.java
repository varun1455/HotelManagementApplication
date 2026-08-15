package com.project.stayEase.service.booking.domain.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentRequest {
    private Long bookingId;

    private BigDecimal amount;

    private String currency;

    private String successUrl;

    private String failureUrl;

    private String productName;

    private String productDescription;

    private Long quantity;
}

package com.project.stayEase.service.booking.domain.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RefundRequest {

    private String providerPaymentId;
    private BigDecimal amount;
    private String reason;

}

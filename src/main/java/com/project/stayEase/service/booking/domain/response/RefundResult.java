package com.project.stayEase.service.booking.domain.response;

import com.project.stayEase.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RefundResult {
    private boolean successful;

    private String refundId;

    private BigDecimal amount;

    private PaymentStatus status;

    private String failureReason;
}

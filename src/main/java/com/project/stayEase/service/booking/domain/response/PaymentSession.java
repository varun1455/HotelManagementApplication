package com.project.stayEase.service.booking.domain.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentSession {
    private String sessionId;

    private String checkoutUrl;

    private String provider;

    private String paymentReference;

}

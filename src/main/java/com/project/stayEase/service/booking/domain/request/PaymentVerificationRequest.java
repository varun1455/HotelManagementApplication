package com.project.stayEase.service.booking.domain.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentVerificationRequest {

    private String providerPayload;

    private String providerSignature;
}

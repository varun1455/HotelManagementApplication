package com.project.stayEase.service.booking.services.provider;


import com.project.stayEase.entity.User;
import com.project.stayEase.service.booking.domain.request.PaymentRequest;
import com.project.stayEase.service.booking.domain.request.PaymentVerificationRequest;
import com.project.stayEase.service.booking.domain.request.RefundRequest;
import com.project.stayEase.service.booking.domain.response.PaymentResult;
import com.project.stayEase.service.booking.domain.response.PaymentSession;
import com.project.stayEase.service.booking.domain.response.RefundResult;

public interface PaymentProvider {

    PaymentSession createCheckoutSession(PaymentRequest request, User user);
    PaymentResult processWebhook(PaymentVerificationRequest request);
    RefundResult refund(RefundRequest request);

}

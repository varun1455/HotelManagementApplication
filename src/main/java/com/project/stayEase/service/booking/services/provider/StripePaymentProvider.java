package com.project.stayEase.service.booking.services.provider;

import com.project.stayEase.entity.User;
import com.project.stayEase.service.booking.domain.request.PaymentRequest;
import com.project.stayEase.service.booking.domain.request.PaymentVerificationRequest;
import com.project.stayEase.service.booking.domain.request.RefundRequest;
import com.project.stayEase.service.booking.domain.response.PaymentResult;
import com.project.stayEase.service.booking.domain.response.PaymentSession;
import com.project.stayEase.service.booking.domain.response.RefundResult;
import com.project.stayEase.service.booking.stripe.StripeCheckoutService;
import com.project.stayEase.service.booking.stripe.StripeCustomerService;
import com.project.stayEase.service.booking.stripe.StripeRefundService;
import com.project.stayEase.service.booking.stripe.StripeWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripePaymentProvider implements PaymentProvider {


    private final StripeCheckoutService stripeCheckoutService;
    private final StripeWebhookService stripeWebhookService;
    private final StripeRefundService stripeRefundService;
    private final StripeCustomerService stripeCustomerService;



    @Override
    public PaymentSession createCheckoutSession(PaymentRequest request, User user) {
            String customerId = stripeCustomerService.getOrCreateCustomer(user);
        return stripeCheckoutService.createCheckoutSession(request, customerId);
    }

    @Override
    public PaymentResult processWebhook(PaymentVerificationRequest request) {
        return stripeWebhookService.verifyPayment(request);
    }

    @Override
    public RefundResult refund(RefundRequest request) {
        return stripeRefundService.createRefund(request);
    }


}

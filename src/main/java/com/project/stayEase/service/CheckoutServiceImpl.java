package com.project.stayEase.service;


import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.User;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.PaymentRepository;
import com.project.stayEase.security.SecurityUtils;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService{

    private final SecurityUtils securityUtils;
    private final PaymentRepository paymentRepository;

    @Override
    public String getCheckoutSession(Booking booking, String successUrl, String failureUrl) {

        User user = securityUtils.getCurrentuser();

        try {

            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();
            Customer customer = Customer.create(customerCreateParams);

            SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("inr")
                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(booking.getHotel().getName())
                                                    .build()
                                    )
                                    .build())
                            .build())
                    .build();

            Session session = Session.create(sessionCreateParams);

            Payment payment = Payment.builder()
                    .checkoutSessionId(session.getId())
                    .transactionId(session.getId())
                    .amount(booking.getAmount())
                    .paymentStatus(PaymentStatus.PENDING)
                    .booking(booking)
                    .build();
            paymentRepository.save(payment);

            return session.getUrl();
        }catch (StripeException stripeException){

            throw new RuntimeException(stripeException);
        }



    }
}

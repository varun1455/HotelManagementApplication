package com.project.stayEase.service.booking.stripe;

import com.project.stayEase.customExceptions.PaymentRefundException;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.service.booking.domain.request.RefundRequest;
import com.project.stayEase.service.booking.domain.response.RefundResult;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeRefundService {

    public RefundResult createRefund(RefundRequest request){

        try{
            RefundCreateParams.Builder builder =
                    RefundCreateParams.builder()
                            .setPaymentIntent(
                                    request.getProviderPaymentId()
                            )
                            .setReason(
                                    RefundCreateParams.Reason
                                            .REQUESTED_BY_CUSTOMER
                            );

            if (request.getAmount() != null) {

                long amountInPaise = request.getAmount()
                        .multiply(BigDecimal.valueOf(100))
                        .longValue();

                builder.setAmount(amountInPaise);
            }

        Refund refund =  Refund.create(builder.build());
            return RefundResult.builder()
                    .successful(true)
                    .refundId(refund.getId())
                    .amount(request.getAmount())
                    .status(PaymentStatus.REFUNDED)
                    .failureReason(null)
                    .build();

        } catch (StripeException e) {

            throw new PaymentRefundException("Failed to refund payment for booking",e);
        }

    }

}

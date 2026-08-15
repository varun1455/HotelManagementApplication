package com.project.stayEase.service.booking.services.cancellation;

import com.project.stayEase.customExceptions.PaymentRefundException;
import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.Payment;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.PaymentRepository;
import com.project.stayEase.service.booking.domain.request.RefundRequest;
import com.project.stayEase.service.booking.domain.response.RefundResult;
import com.project.stayEase.service.booking.services.provider.PaymentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundService {


    private final PaymentRepository paymentRepository;
    private final PaymentProvider paymentProvider;

    public void refundBooking(Booking booking){

        Payment payment = paymentRepository.findFirstByBookingIdAndPaymentStatusOrderByCreatedAtDesc(
                booking.getId(), PaymentStatus.APPROVED).orElseThrow(
                        ()->new ResourceNotFoundException("Approved Payment not found"));


        try{


            RefundRequest request = RefundRequest.builder()
                    .providerPaymentId(
                            payment.getPaymentIntentId()
                    )
                    .amount(booking.getAmount())
                    .reason("Customer requested booking cancellation")
                    .build();


            RefundResult result =
                    paymentProvider.refund(request);


            payment.setRefundId(result.getRefundId());
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
        } catch (PaymentRefundException e) {

            payment.setPaymentStatus(PaymentStatus.REFUND_FAILED);

            throw e;
        }
    }
}

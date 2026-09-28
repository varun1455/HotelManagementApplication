package com.project.stayEase.controller.booking;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {

    @GetMapping("/payments/success")
    public String paymentSuccess() {
        return "payment-result";
    }

    @GetMapping("/payments/failure")
    public String paymentFailure() {
        return "payment-result";
    }
}

package com.project.stayEase.service;

import com.project.stayEase.entity.Booking;

public interface CheckoutService {

    public String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}

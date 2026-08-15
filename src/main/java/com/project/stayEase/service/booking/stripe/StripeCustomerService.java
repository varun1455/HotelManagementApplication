package com.project.stayEase.service.booking.stripe;

import com.project.stayEase.customExceptions.PaymentProviderException;
import com.project.stayEase.entity.User;
import com.project.stayEase.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeCustomerService {

    private final UserRepository userRepository;
    
    public String getOrCreateCustomer(User user){


        if (user.getPaymentCustomerId() != null) {
            return user.getPaymentCustomerId();
        }

        try {

            CustomerCreateParams params =
                    CustomerCreateParams.builder()
                            .setName(user.getName())
                            .setEmail(user.getEmail())
                            .build();

            Customer customer = Customer.create(params);
            user.setPaymentCustomerId(customer.getId());
            userRepository.save(user);

            return customer.getId();

        } catch (StripeException e) {

            throw new PaymentProviderException(
                    "Unable to create Stripe customer", e);
        }

    }
}

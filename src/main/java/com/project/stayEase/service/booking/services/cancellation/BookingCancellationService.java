package com.project.stayEase.service.booking.services.cancellation;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.repository.BookingRepository;
import com.project.stayEase.security.utils.SecurityUtils;
import com.project.stayEase.service.booking.services.inventory.InventoryReleaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingCancellationService {

    private final SecurityUtils securityUtils;
    private final BookingRepository bookingRepository;
    private final BookingValidationService bookingValidationService;
    private final InventoryReleaseService inventoryReleaseService;
    private final RefundService refundService;

    public void cancelBooking(Long bookingId){

        Long currentUserId = securityUtils.getCurrentUserId();

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found or access denied"));


        bookingValidationService.validateCancellation(booking);
        inventoryReleaseService.releaseInventory(booking);
        refundService.refundBooking(booking);
        booking.setBookingStatus(BookingStatus.CANCELLED);
    }
}

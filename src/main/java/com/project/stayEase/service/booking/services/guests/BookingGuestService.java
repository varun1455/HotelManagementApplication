package com.project.stayEase.service.booking.services.guests;

import com.project.stayEase.customExceptions.GuestCapacityExceededException;
import com.project.stayEase.customExceptions.InvalidBookingStateException;
import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.booking.response.BookingResponseDto;
import com.project.stayEase.dto.guests.request.GuestRequestDto;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.Guest;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.repository.BookingRepository;
import com.project.stayEase.repository.GuestRepository;
import com.project.stayEase.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingGuestService {


    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final SecurityUtils securityUtils;
    private final ModelMapper modelMapper;

    public BookingResponseDto addGuestsToBooking(List<GuestRequestDto> guestRequestDto, Long bookingId) {

        Long currentUserId = securityUtils.getCurrentUserId();

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found or access denied"));

       validateBookingForGuestAddition(booking);
       validateGuestCount(booking, guestRequestDto);


        List<Guest> guests = guestRequestDto.stream()
                .map(dto -> createGuest(dto, booking))
                .toList();

        booking.getGuests().addAll(guests);
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking.setReservedUntil(LocalDateTime.now().plusMinutes(10));
        guestRepository.saveAll(guests);
        bookingRepository.save(booking);
        return  modelMapper.map(booking, BookingResponseDto.class);

    }

    private void validateBookingForGuestAddition(Booking booking) {

        if (booking.getBookingStatus() == BookingStatus.EXPIRED) {
            throw new InvalidBookingStateException("Booking has expired");
        }


        if (booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new InvalidBookingStateException("Booking is not in RESERVED state");
        }
    }

    private void validateGuestCount(Booking booking, List<GuestRequestDto> guests) {

        int maximumGuests = booking.getRoom().getCapacity() * booking.getRoomsCount();

        int existingGuests = booking.getGuests().size();

        int totalGuests = existingGuests + guests.size();

        if (totalGuests > maximumGuests) {
            throw new GuestCapacityExceededException("Maximum " + maximumGuests + " guests are allowed for this booking");
        }
    }

    private Guest createGuest(GuestRequestDto dto,Booking booking) {
        Guest guest = modelMapper.map(dto, Guest.class);
        guest.setUser(booking.getUser());
        return guest;
    }

}

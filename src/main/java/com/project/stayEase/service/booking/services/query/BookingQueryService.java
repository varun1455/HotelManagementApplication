package com.project.stayEase.service.booking.services.query;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.bookingMappers.QueryBookingsDto;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.repository.BookingRepository;
import com.project.stayEase.repository.HotelRepository;
import com.project.stayEase.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingQueryService {

    private final HotelRepository hotelRepository;
    private final SecurityUtils securityUtils;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    public List<QueryBookingsDto> getAllBookingsByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + hotelId));
        if(!hotel.getOwner().getId().equals(securityUtils.getCurrentUserId())){
            throw new AccessDeniedException("You are not Owner of this Hotel");
        }

        List<Booking> bookings = bookingRepository.findByHotel(hotel);

        return bookings.stream()
                .map(booking -> modelMapper.map(booking, QueryBookingsDto.class))
                .collect(Collectors.toList());
    }

}

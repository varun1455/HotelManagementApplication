package com.project.stayEase.service.booking.services.creation;

import com.project.stayEase.dto.bookingMappers.BookingRequestDto;
import com.project.stayEase.dto.bookingMappers.BookingResponseDto;
import com.project.stayEase.entity.Booking;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.Room;
import com.project.stayEase.repository.BookingRepository;
import com.project.stayEase.security.SecurityUtils;
import com.project.stayEase.service.booking.services.inventory.InventoryReservationService;
import com.project.stayEase.service.hotel.HotelService;
import com.project.stayEase.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingCreationService {

    private final HotelService hotelService;
    private final RoomService roomService;
    private final InventoryReservationService inventoryReservationService;
    private final BookingRepository bookingRepository;
    private final SecurityUtils securityUtils;
    private final ModelMapper modelMapper;
    private final BookingPricingService bookingPricingService;
    private final BookingFactory bookingFactory;

    public BookingResponseDto initializeBooking(BookingRequestDto bookingRequestDto){
        Hotel hotel = hotelService.getActiveHotel(bookingRequestDto.getHotelId());

        Room room = roomService.getRoomOfHotel(bookingRequestDto.getRoomId());

        List<Inventory> inventoryList = inventoryReservationService.reserveInventoryForBooking(room.getId(),
                bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate(), bookingRequestDto.getRoomsCount());

        BigDecimal totalAmount = bookingPricingService.calculateFinalAmount(inventoryList, bookingRequestDto.getRoomsCount());

        Booking booking = bookingFactory.createReservedBooking(securityUtils.getCurrentuser(), hotel, room, bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate(),bookingRequestDto.getRoomsCount(), totalAmount);

        bookingRepository.save(booking);

        return modelMapper.map(booking, BookingResponseDto.class);
    }

}

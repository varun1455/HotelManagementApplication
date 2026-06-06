package com.project.stayEase.service;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.BookingRequestDto;
import com.project.stayEase.dto.BookingResponseDto;
import com.project.stayEase.dto.GuestRequestDto;
import com.project.stayEase.entity.*;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{


    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingResponseDto initializeBooking(BookingRequestDto bookingRequestDto) {

        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId()).
                orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + bookingRequestDto.getHotelId()));

        Room room = roomRepository.findById(bookingRequestDto.getRoomId()).
                orElseThrow(()->new ResourceNotFoundException("Room not found with id " + bookingRequestDto.getRoomId()));


        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate(), bookingRequestDto.getRoomsCount());

        Long totalDays = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate()) + 1;

        if(inventoryList.size() != totalDays){
            throw new IllegalStateException("Room is not available anymore");
        }

        for(Inventory inventory: inventoryList){
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequestDto.getRoomsCount());
        }


        inventoryRepository.saveAll(inventoryList);
        
        Booking booking = Booking.builder()
                .hotel(hotel)
                .room(room)
                .roomsCount(bookingRequestDto.getRoomsCount())
                .user(getCurrentuser())
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .bookingStatus(BookingStatus.RESERVED)
                .amount(BigDecimal.TEN)
                .build();

        

         bookingRepository.save(booking);
         return modelMapper.map(booking, BookingResponseDto.class);


    }

    @Override
    public BookingResponseDto addGuestsToBooking(List<GuestRequestDto> guestRequestDto, Long bookingId) {

        log.info("adding guests started");

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found with id " + bookingId));

        log.info(booking.toString());

        log.info("booking id found");

        if(isBookingExpired(booking)){
            throw new IllegalStateException("Booking has been expired");
        }

        log.info("booking not expired");

        if(booking.getBookingStatus()!=BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not in reserved state, cannot add guests");
        }

        log.info("booking is not in reserved state");

        booking.setBookingStatus(BookingStatus.ADDING_GUESTS);
//        Set<Guest> guests = guestRequestDto.stream()
//                .map(e-> modelMapper.map(e, Guest.class))
//                .collect(Collectors.toSet());

        log.info("let's add guests now");

        for(GuestRequestDto guest : guestRequestDto){
            Guest g = modelMapper.map(guest, Guest.class);
            g.setUser(getCurrentuser());
            guestRepository.save(g);
            booking.getGuests().add(g);
        }

        bookingRepository.save(booking);
        return  modelMapper.map(booking, BookingResponseDto.class);


    }

    public boolean isBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentuser(){
        User user = new User();
        user.setId(1L);
        return user;
    }
}

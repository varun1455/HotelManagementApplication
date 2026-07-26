package com.project.stayEase.service;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.*;
import com.project.stayEase.entity.*;
import com.project.stayEase.entity.enums.BookingStatus;
import com.project.stayEase.entity.enums.PaymentStatus;
import com.project.stayEase.repository.*;
import com.project.stayEase.security.SecurityUtils;
import com.project.stayEase.service.strategy.PricingService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
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
    private final SecurityUtils securityUtils;
    private final CheckoutService checkoutService;
    private final PaymentRepository paymentRepository;
    private final PricingService pricingService;


    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public BookingResponseDto initializeBooking(BookingRequestDto bookingRequestDto) {

        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId()).
                orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + bookingRequestDto.getHotelId()));

        Room room = roomRepository.findById(bookingRequestDto.getRoomId()).
                orElseThrow(()->new ResourceNotFoundException("Room not found with id " + bookingRequestDto.getRoomId()));


        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate(), bookingRequestDto.getRoomsCount());

        long totalDays = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate()) + 1;

        if(inventoryList.size() != totalDays){
            throw new IllegalStateException("Room is not available anymore");
        }

        inventoryRepository.initBooking(room.getId(), bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate(), bookingRequestDto.getRoomsCount());

        BigDecimal totalPrice = pricingService.totalPriceOfRoomFromCheckinToCheckoutDate(inventoryList);
        BigDecimal totalPriceForAllRooms = totalPrice.multiply(BigDecimal.valueOf(bookingRequestDto.getRoomsCount()));
        
        Booking booking = Booking.builder()
                .hotel(hotel)
                .room(room)
                .roomsCount(bookingRequestDto.getRoomsCount())
                .user(securityUtils.getCurrentuser())
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .bookingStatus(BookingStatus.RESERVED)
                .amount(totalPriceForAllRooms)
                .reservedUntil(LocalDateTime.now().plusMinutes(10))
                .build();

         bookingRepository.save(booking);
         return modelMapper.map(booking, BookingResponseDto.class);


    }

    @Override
    public BookingResponseDto addGuestsToBooking(List<GuestRequestDto> guestRequestDto, Long bookingId) {

        log.info("adding guests started");

        Long currentUserId = securityUtils.getCurrentUserId();

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found or access denied"));


        log.info(booking.toString());

        log.info("booking id found");

        if(booking.getBookingStatus() == BookingStatus.EXPIRED){
            throw new IllegalStateException("Booking has been expired");
        }

        log.info("booking not expired");

        if(booking.getBookingStatus()!=BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not in reserved state, cannot add guests");
        }

        log.info("booking is not in reserved state");

        booking.setBookingStatus(BookingStatus.ADDING_GUESTS);

        log.info("let's add guests now");

        for(GuestRequestDto guest : guestRequestDto){
            Guest g = modelMapper.map(guest, Guest.class);
            g.setUser(securityUtils.getCurrentuser());
            guestRepository.save(g);
            booking.getGuests().add(g);
        }

        bookingRepository.save(booking);
        return  modelMapper.map(booking, BookingResponseDto.class);


    }

    @Override
    public PaymentSessionDto initiatePayment(Long bookingId) {

        Long currentUserId = securityUtils.getCurrentUserId();
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found or access denied"));

        if(booking.getBookingStatus() == BookingStatus.EXPIRED){
            throw new IllegalStateException("Booking has been expired");
        }

        String sessionUrl = checkoutService.getCheckoutSession(booking,
                frontendUrl + "payments/success", frontendUrl + "payments/failure");

        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        booking.setPaymentInitiatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return PaymentSessionDto.builder()
                .sessionUrl(sessionUrl)
                .build();
    }

    @Override
    @Transactional
    public void capturePaymentEvent(Event event) {

        if("checkout.session.completed".equals(event.getType())){
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if(session == null) return;
            String sessionId = session.getId();
            log.info("sessionId {}", sessionId);
            log.info("payment status {}", session.getPaymentStatus());

           Payment payment = paymentRepository.findByCheckoutSessionId(sessionId).orElseThrow(()->new ResourceNotFoundException("Payment not found with session id " + sessionId));

            if (payment.getPaymentStatus() == PaymentStatus.APPROVED) {
                log.info("Payment already processed {}", sessionId);
                return;
            }


           if("paid".equals(session.getPaymentStatus())) {
               payment.setPaymentIntentId(session.getPaymentIntent());
               payment.setPaymentStatus(PaymentStatus.APPROVED);
               Booking booking = payment.getBooking();
               booking.setBookingStatus(BookingStatus.CONFIRMED);

               inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
               inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());

               log.info("Booking Successfully Confirmed with bookingId={} and sessionId={}", booking.getId(), payment.getCheckoutSessionId());

           }else{
               log.warn(
                       "Checkout completed but payment not paid. SessionId={}, status={}",
                       sessionId,
                       session.getPaymentStatus());
           }


        }else{
            log.warn("Unhandled event type {}", event.getType());
        }

    }

    @Override
    @Transactional
    public void cancelMyBooking(Long bookingId) {
        Long currentUserId = securityUtils.getCurrentUserId();
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, currentUserId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found or access denied"));

        if(booking.getBookingStatus() != BookingStatus.CONFIRMED){
            throw new IllegalStateException("You cannot cancel this booking with bookingId " + bookingId);
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(), booking.getRoomsCount());

        int rowsEffected = inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),booking.getCheckOutDate(), booking.getRoomsCount());
        int totalDays = Math.toIntExact(ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate()) + 1);
        if(rowsEffected != totalDays ){
            throw new IllegalStateException("Inventory update failed");
        }

        Payment payment = paymentRepository.findFirstByBookingIdAndPaymentStatusOrderByCreatedAtDesc(bookingId, PaymentStatus.APPROVED).orElseThrow(()->new ResourceNotFoundException("Approved Payment not found"));
        try{
            RefundCreateParams refundCreateParams = RefundCreateParams.builder()
                    .setPaymentIntent(payment.getPaymentIntentId())
                    .setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER)
                    .putMetadata("bookingId", booking.getId().toString())
                    .putMetadata("cancelledBy", currentUserId.toString())
                    .build();

            Refund refund = Refund.create(refundCreateParams);
            payment.setRefundId(refund.getId());
            payment.setPaymentStatus(PaymentStatus.REFUNDED);


        } catch (StripeException e) {
            throw new RuntimeException(e);
        }


    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void updateInventoryForExpiredBookings(){

        List<Booking> reservedBookings = bookingRepository.findExpiredBookings(BookingStatus.RESERVED, LocalDateTime.now());
        for(Booking booking : reservedBookings){
            inventoryRepository.releasedInventoryForExpiredBooking(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
            booking.setBookingStatus(BookingStatus.EXPIRED);
        }

        List<Booking> paymentPendingBookings = bookingRepository.findPaymentPendingBookings(BookingStatus.PAYMENT_PENDING, LocalDateTime.now().minusMinutes(15));
        for(Booking booking : paymentPendingBookings){
            inventoryRepository.releasedInventoryForExpiredBooking(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
            booking.setBookingStatus(BookingStatus.EXPIRED);
        }

    }

    @Override
    public List<BookingsPerHotelDto> getAllBookingsByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + hotelId));
        if(!hotel.getOwner().getId().equals(securityUtils.getCurrentUserId())){
            throw new AccessDeniedException("You are not Owner of Hotel");
        }

        List<Booking> bookings = bookingRepository.findByHotel(hotel);

        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingsPerHotelDto.class))
                .collect(Collectors.toList());


    }


}

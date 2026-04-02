package com.project.stayEase.entity;

import com.project.stayEase.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "hotel_id", nullable = false)
    private Hotel hotel;


    @ManyToOne
    @JoinColumn(name= "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private Integer roomsCount;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable = false)
    private User user;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="payment_id")
    private Payment payment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="booking_guests",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name="guest_id"))
    private Set<Guest> guests;
}

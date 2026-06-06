package com.project.stayEase.repository;

import com.project.stayEase.dto.HotelMinPriceDto;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice, Long> {



    @Query("""
       SELECT new com.project.stayEase.dto.HotelMinPriceDto(hmp.hotel, MIN(hmp.price))
       FROM HotelMinPrice hmp
       WHERE hmp.hotel.city = :city
             AND hmp.date BETWEEN :startDate AND :endDate
             AND hmp.hotel.active = true
       GROUP BY hmp.hotel
""")
    Page<HotelMinPriceDto>findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
            );


    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);

}




///  here we are finding the hotel with the min room price and show that hotels in that date range

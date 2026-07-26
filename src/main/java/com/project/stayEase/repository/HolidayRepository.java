package com.project.stayEase.repository;

import com.project.stayEase.entity.Holiday;
import com.project.stayEase.entity.enums.HolidayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);


    @Query("""
        SELECT h
        FROM Holiday h
        WHERE h.date IN :dates
""")
    List<Holiday> findByDateIn(@Param("dates") List<LocalDate> dates);


    @Query("""
      SELECT h
      FROM Holiday h
      WHERE h.type = :holidayType
        AND h.date between :startDate AND :endDate
""")
    List<Holiday> findByTypeAndDateBetween(
            @Param("holidayType") HolidayType holidayType,
             @Param("startDate" )LocalDate today,
            @Param("endDate") LocalDate lastCalculated);
}

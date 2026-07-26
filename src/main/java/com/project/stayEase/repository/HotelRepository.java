package com.project.stayEase.repository;

import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByOwner(User user);
}
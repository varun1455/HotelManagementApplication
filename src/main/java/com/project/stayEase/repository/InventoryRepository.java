package com.project.stayEase.repository;

import com.project.stayEase.entity.Inventory;
import com.project.stayEase.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByRoom(Room room);
}
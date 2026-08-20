package com.project.stayEase.service.room;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.roomMappers.RoomRequestDto;
import com.project.stayEase.dto.roomMappers.RoomUpdateResult;
import com.project.stayEase.entity.BedType;
import com.project.stayEase.entity.Room;
import com.project.stayEase.entity.RoomType;
import com.project.stayEase.repository.BedTypeRepository;
import com.project.stayEase.repository.InventoryRepository;
import com.project.stayEase.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RoomUpdateService {

    private final RoomTypeRepository roomTypeRepository;
    private final BedTypeRepository bedTypeRepository;
    private final InventoryRepository inventoryRepository;


    public RoomUpdateResult update(Room room, RoomRequestDto request) {

        boolean priceChanged = false;
        boolean quantityChanged = false;

        if (request.getBasePrice() != null && room.getBasePrice().compareTo(request.getBasePrice()) != 0) {
            room.setBasePrice(request.getBasePrice());
            priceChanged = true;
        }

        if (request.getTotalCount() != null && !room.getTotalCount().equals(request.getTotalCount())) {
            validateQuantity(room, request.getTotalCount());
            room.setTotalCount(request.getTotalCount());
            quantityChanged = true;
        }

        if (request.getRoomTypeId() != null) {
            RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId()).orElseThrow(() ->
                            new ResourceNotFoundException("RoomType not found"));
            room.setType(roomType);
        }

        if (request.getBedTypeId() != null) {

            BedType bedType = bedTypeRepository.findById(request.getBedTypeId()).orElseThrow(() ->
                            new ResourceNotFoundException("BedType not found"));

            room.setBedType(bedType);
        }

        if (request.getPhotos() != null) {
            room.setPhotos(request.getPhotos());
        }

        if (request.getAmenities() != null) {
            room.setAmenities(request.getAmenities());
        }

        if (request.getCapacity() != null) {
            room.setCapacity(request.getCapacity());
        }

        return new RoomUpdateResult(priceChanged, quantityChanged);
    }
    private void validateQuantity(Room room, Integer newTotalCount) {

        if (newTotalCount < 0) {
            throw new IllegalArgumentException("Total count cannot be negative");
        }

        if (newTotalCount < room.getTotalCount()) {

            boolean invalid = inventoryRepository.existsBookedCountGreaterThan(room, LocalDate.now(), newTotalCount);

            if (invalid) {
                throw new IllegalArgumentException("Total room count cannot be less than already booked rooms");
            }
        }
    }


}

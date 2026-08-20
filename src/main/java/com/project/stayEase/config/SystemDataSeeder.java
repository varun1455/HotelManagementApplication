package com.project.stayEase.config;

import com.project.stayEase.entity.BedType;
import com.project.stayEase.entity.RoomType;
import com.project.stayEase.repository.BedTypeRepository;
import com.project.stayEase.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SystemDataSeeder implements CommandLineRunner {

    private final RoomTypeRepository roomTypeRepository;
    private final BedTypeRepository bedTypeRepository;
    private final SeedProperties seedProperties;

    @Override
    public void run(String... args) {

        if (!seedProperties.isEnabled()) {
            return;
        }

        seedRoomTypes();
        seedBedTypes();
    }

    private void seedRoomTypes() {

        for (String name : seedProperties.getRoomTypes()) {

            if (!roomTypeRepository.existsByNameIgnoreCase(name)) {

                RoomType roomType = new RoomType();

                roomType.setName(name);
                roomType.setAvailable(true);

                roomTypeRepository.save(roomType);
            }
        }
    }

    private void seedBedTypes() {

        for (String name : seedProperties.getBedTypes()) {

            if (!bedTypeRepository.existsByNameIgnoreCase(name)) {

                BedType bedType = new BedType();

                bedType.setName(name);

                bedTypeRepository.save(bedType);
            }
        }
    }
}

package com.project.stayEase.config;

import com.project.stayEase.dto.RoomRequestDto;
import com.project.stayEase.dto.RoomResponseDto;
import com.project.stayEase.entity.Room;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class modelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.typeMap(RoomRequestDto.class, Room.class)
                .addMappings(m -> {
                    m.skip(Room::setType);
                    m.skip(Room::setBedType);
                    m.skip(Room::setHotel);
                });
        mapper.typeMap(Room.class, RoomResponseDto.class)
                .addMappings(m -> {
                    m.map(src -> src.getType().getId(),
                            RoomResponseDto::setRoomTypeId);

                    m.map(src -> src.getBedType().getId(),
                            RoomResponseDto::setBedTypeId);
                });
        return mapper;
    }
}

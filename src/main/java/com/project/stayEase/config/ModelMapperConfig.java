package com.project.stayEase.config;

import com.project.stayEase.dto.bookingMappers.summaryMappers.RoomSummaryDtoForBooking;
import com.project.stayEase.dto.roomMappers.BedTypeResponseDto;
import com.project.stayEase.dto.roomMappers.RoomRequestDto;
import com.project.stayEase.dto.roomMappers.RoomResponseDto;
import com.project.stayEase.dto.roomMappers.RoomTypeResponseDto;
import com.project.stayEase.entity.BedType;
import com.project.stayEase.entity.Room;
import com.project.stayEase.entity.RoomType;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

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

        mapper.typeMap(Room.class, RoomSummaryDtoForBooking.class)
                .addMappings(m -> {

                    m.map(src -> src.getType().getId(),
                            RoomSummaryDtoForBooking::setRoomTypeId);

                    m.map(src -> src.getBedType().getId(),
                            RoomSummaryDtoForBooking::setBedTypeId);
                });

        mapper.typeMap(RoomType.class, RoomTypeResponseDto.class)
                .addMappings(m->{
                    m.map(RoomType::getName,
                            RoomTypeResponseDto::setName);
                    m.map(RoomType::getId,
                            RoomTypeResponseDto::setId);
                });

        mapper.typeMap(BedType.class, BedTypeResponseDto.class)
                .addMappings(m->{
                    m.map(BedType::getName,
                            BedTypeResponseDto::setName);
                    m.map(BedType::getId,
                            BedTypeResponseDto::setId);
                });
        return mapper;
    }
}

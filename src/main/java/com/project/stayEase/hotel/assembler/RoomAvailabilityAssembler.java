package com.project.stayEase.hotel.assembler;

import com.project.stayEase.dto.RoomSearchResponseDto;
import com.project.stayEase.hotel.projection.RoomAvailabilityProjection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomAvailabilityAssembler {

        private final ModelMapper modelMapper;

    public Map<Long, List<RoomSearchResponseDto>> availableRoomsInEachHotel(List<RoomAvailabilityProjection> roomAvailabilityProjections){

       return roomAvailabilityProjections.stream()
                .collect(Collectors.groupingBy(RoomAvailabilityProjection::getHotelId,
                                Collectors.mapping(
                                        projection-> {
                                            RoomSearchResponseDto roomSearchResponseDto = modelMapper.map(projection.getRoom(), RoomSearchResponseDto.class);
                                            roomSearchResponseDto.setAvailableNumberOfRooms(projection.getAvailableRooms());
                                            roomSearchResponseDto.setTotalPrice(projection.getTotalPrice());
                                            return roomSearchResponseDto;
                                        },
                                        Collectors.toList()
                                )
                        )
                );

    }

    public Map<Long, BigDecimal> getMinTotalPrice(List<RoomAvailabilityProjection> roomAvailabilityProjections){

        return roomAvailabilityProjections.stream()
            .collect(Collectors.groupingBy(
                    RoomAvailabilityProjection::getHotelId,
                    Collectors.collectingAndThen(
                            Collectors.minBy(
                                    Comparator.comparing(RoomAvailabilityProjection::getTotalPrice)
                            ),
                            projection ->
                                    projection.map(RoomAvailabilityProjection::getTotalPrice)
                                            .orElse(BigDecimal.ZERO)
                    )
            ));
    }

}

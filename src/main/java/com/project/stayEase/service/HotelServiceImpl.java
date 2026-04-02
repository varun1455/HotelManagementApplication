package com.project.stayEase.service;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.HotelRequestDto;
import com.project.stayEase.dto.HotelResponseDto;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService{


    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;


    @Override
    public HotelResponseDto createNewHotel(HotelRequestDto hotelRequestDto) {

        log.info("creating new hotel with hotelRequestDto={}", hotelRequestDto.getName());
        Hotel hotel = modelMapper.map(hotelRequestDto, Hotel.class);
        hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelResponseDto.class);

    }

    @Override
    public HotelResponseDto getHotelById(Long id) {
        log.info("getting hotel with id={}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + id));
        return modelMapper.map(hotel, HotelResponseDto.class);
    }

    @Override
    public HotelResponseDto updateHotelById(Long id, HotelRequestDto hotelRequestDto) {
        log.info("updating hotel with id={}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + id));
        modelMapper.typeMap(HotelRequestDto.class, Hotel.class)
                .addMappings(mapper -> {
                    mapper.skip(Hotel::setAmenities);
                    mapper.skip(Hotel::setPhotos);
                });
        hotel.setAmenities(new ArrayList<>(Arrays.asList(hotelRequestDto.getAmenities())).toArray(new String[0]));
        hotel.setPhotos(new ArrayList<>(Arrays.asList(hotelRequestDto.getPhotos())).toArray(new String[0]));
        hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelResponseDto.class);
    }

    @Override
    public void activateHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + id));
        hotel.setActive(true);
        hotelRepository.save(hotel);
    }
}

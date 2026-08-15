package com.project.stayEase.service.hotel;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import com.project.stayEase.dto.hotelMappers.HotelInfoDto;
import com.project.stayEase.dto.hotelMappers.HotelRequestDto;
import com.project.stayEase.dto.hotelMappers.HotelResponseDto;
import com.project.stayEase.dto.roomMappers.RoomResponseDto;
import com.project.stayEase.entity.Hotel;
import com.project.stayEase.entity.Room;
import com.project.stayEase.entity.User;
import com.project.stayEase.service.inventory.InventoryService;
import com.project.stayEase.repository.HotelRepository;
import com.project.stayEase.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {


    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final SecurityUtils securityUtils;


    @Override
    public HotelResponseDto createNewHotel(HotelRequestDto hotelRequestDto) {

        log.info("creating new hotel with hotelRequestDto={}", hotelRequestDto.getName());
        Hotel hotel = modelMapper.map(hotelRequestDto, Hotel.class);
        hotel.setOwner(securityUtils.getCurrentuser());
        hotelRepository.save(hotel);
        return modelMapper.map(hotel, HotelResponseDto.class);

    }

    @Override
    public HotelResponseDto getHotelById(Long id) {
        log.info("getting hotel with id={}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + id));
        if(!hotel.getOwner().getId().equals(securityUtils.getCurrentUserId())){
            throw new AccessDeniedException("You are not access this hotel");
        }
        return modelMapper.map(hotel, HotelResponseDto.class);
    }

    @Override
    public HotelResponseDto updateHotelById(Long id, HotelRequestDto hotelRequestDto) {
        log.info("updating hotel with id={}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + id));
        if(!hotel.getOwner().getId().equals(securityUtils.getCurrentUserId())){
            throw new AccessDeniedException("You are not allowed to modify this hotel");
        }
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
        if(!hotel.getOwner().getId().equals(securityUtils.getCurrentUserId())){
            throw new AccessDeniedException("You are not access this hotel");
        }
        hotel.setActive(true);
        for(Room room: hotel.getRooms()){
            inventoryService.initializeRoom(room);
        }

    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + id));
        if(!hotel.getOwner().getId().equals(securityUtils.getCurrentUserId())){
            throw new AccessDeniedException("You are not allowed to delete this hotel");
        }
        for(Room room: hotel.getRooms()){
            inventoryService.deleteInventories(room);
        }
        hotelRepository.deleteById(id);

    }

    @Override
    public HotelInfoDto findHotelInfo(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel not found with id " + hotelId));

        List<RoomResponseDto> rooms = hotel.getRooms().stream()
                .map(room-> modelMapper.map(room, RoomResponseDto.class))
                .toList();

        return new HotelInfoDto(modelMapper.map(hotel, HotelResponseDto.class), rooms);
    }

    @Override
    public List<HotelResponseDto> getAllHotelsOfOwner() {
        User user = securityUtils.getCurrentuser();
        List<Hotel> hotels = hotelRepository.findByOwner(user);

        return hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelResponseDto.class))
                .toList();

    }

    public Hotel getActiveHotel(Long id){

        return hotelRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hotel not found"));
    }
}

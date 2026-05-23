package com.project.stayEase.controller;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.RoomRequestDto;
import com.project.stayEase.dto.RoomResponseDto;
import com.project.stayEase.repository.BedTypeRepository;
import com.project.stayEase.repository.RoomRepository;
import com.project.stayEase.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoomResponseDto>> createRoom(@PathVariable Long hotelId, @RequestBody RoomRequestDto roomRequestDto) {
        RoomResponseDto roomResponseDto = roomService.createRoom(hotelId,roomRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(roomResponseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getAllRoomsInHotel(@PathVariable Long hotelId) {
        List<RoomResponseDto> roomResponseDtos = roomService.getAllRoomsInHotel(hotelId);
        return new ResponseEntity<>(ApiResponse.successResponse(roomResponseDtos), HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> getRoomById(@PathVariable Long roomId) {
        RoomResponseDto roomResponseDto = roomService.getRoomById(roomId);
        return new ResponseEntity<>(ApiResponse.successResponse(roomResponseDto), HttpStatus.OK);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> updateRoom(@PathVariable Long roomId, @RequestBody RoomRequestDto roomRequestDto) {
        RoomResponseDto roomResponseDto = roomService.updateRoom(roomId, roomRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(roomResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }
}

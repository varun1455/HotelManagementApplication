package com.project.stayEase.controller;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.RoomTypeRequestDto;
import com.project.stayEase.dto.RoomTypeResponseDto;
import com.project.stayEase.service.RoomTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roomtype")
@AllArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoomTypeResponseDto>> createRoomType(@RequestBody RoomTypeRequestDto roomTypeRequestDto) {
        RoomTypeResponseDto roomTypeResponseDto = roomTypeService.createRoomType(roomTypeRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(roomTypeResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/{roomtypeId}")
    public ResponseEntity<ApiResponse<RoomTypeResponseDto>> getRoomType(@PathVariable Long roomtypeId) {
        RoomTypeResponseDto roomTypeResponseDto = roomTypeService.findRoomType(roomtypeId);
        return new ResponseEntity<>(ApiResponse.successResponse(roomTypeResponseDto), HttpStatus.OK);
    }

    @GetMapping("/allTypes")
    public ResponseEntity<ApiResponse<List<RoomTypeResponseDto>>> getAllRoomTypes() {
        List<RoomTypeResponseDto> roomTypeResponseDtos = roomTypeService.findAllRoomTypes();
        return new ResponseEntity<>(ApiResponse.successResponse(roomTypeResponseDtos), HttpStatus.OK);
    }

}

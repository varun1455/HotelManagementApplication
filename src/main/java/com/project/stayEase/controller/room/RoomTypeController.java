package com.project.stayEase.controller.room;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.roomType.request.RoomTypeRequestDto;
import com.project.stayEase.dto.roomType.response.RoomTypeResponseDto;
import com.project.stayEase.service.room.RoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomtype")
@AllArgsConstructor
@Tag(
        name = "Room Type Management",
        description = "System-defined room types"
)
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @Operation(
            summary = "Create a room type",
            description = "Creates a new system-level room type. Only system administrators can perform this operation."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<RoomTypeResponseDto>> createRoomType(@RequestBody RoomTypeRequestDto roomTypeRequestDto) {
        RoomTypeResponseDto roomTypeResponseDto = roomTypeService.createRoomType(roomTypeRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(roomTypeResponseDto), HttpStatus.CREATED);
    }


    @Operation(
            summary = "Get room type by ID",
            description = "Returns a system-defined room type."
    )
    @GetMapping("/{roomtypeId}")
    public ResponseEntity<ApiResponse<RoomTypeResponseDto>> getRoomType(@PathVariable Long roomtypeId) {
        RoomTypeResponseDto roomTypeResponseDto = roomTypeService.findRoomType(roomtypeId);
        return new ResponseEntity<>(ApiResponse.successResponse(roomTypeResponseDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all room types",
            description = "Returns all available system-defined room types."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomTypeResponseDto>>> getAllRoomTypes() {
        List<RoomTypeResponseDto> roomTypeResponseDtos = roomTypeService.findAllRoomTypes();
        return new ResponseEntity<>(ApiResponse.successResponse(roomTypeResponseDtos), HttpStatus.OK);
    }

}

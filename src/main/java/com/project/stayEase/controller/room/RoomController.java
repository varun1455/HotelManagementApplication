package com.project.stayEase.controller.room;

import com.project.stayEase.advices.ApiResponse;
import com.project.stayEase.dto.room.request.RoomRequestDto;
import com.project.stayEase.dto.room.response.RoomResponseDto;
import com.project.stayEase.service.room.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@AllArgsConstructor
@Tag(
        name = "Room Management",
        description = "Room management APIs for hotel managers"
)
@SecurityRequirement(name = "bearerAuth")
public class RoomController {

    private final RoomService roomService;

    @Operation(
            summary = "Create a room",
            description = "Creates a new room for the specified hotel."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<RoomResponseDto>> createRoom(
            @Parameter(
            description = "ID of the hotel",
            example = "1"
            )@PathVariable Long hotelId,
            @RequestBody RoomRequestDto roomRequestDto) {
        RoomResponseDto roomResponseDto = roomService.createRoom(hotelId,roomRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(roomResponseDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all rooms in a hotel",
            description = "Returns all rooms belonging to the specified hotel."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomResponseDto>>> getAllRoomsInHotel(
            @Parameter(
                    description = "ID of the hotel",
                    example = "1"
            )@PathVariable Long hotelId) {
        List<RoomResponseDto> roomResponseDtos = roomService.getAllRoomsInHotel(hotelId);
        return new ResponseEntity<>(ApiResponse.successResponse(roomResponseDtos), HttpStatus.OK);
    }

    @Operation(
            summary = "Get room by ID",
            description = "Returns details of a specific room."
    )
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> getRoomById(
            @Parameter(
                    description = "ID of the room",
                    example = "1"
            )@PathVariable Long roomId) {
        RoomResponseDto roomResponseDto = roomService.getRoomById(roomId);
        return new ResponseEntity<>(ApiResponse.successResponse(roomResponseDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Update a room",
            description = """
                Updates the specified room.

                Depending on the fields provided, room pricing,
                inventory and dynamic pricing may also be updated.
                """
    )
    @PatchMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponseDto>> updateRoom(
            @Parameter(
                    description = "ID of the room",
                    example = "1"
            )@PathVariable Long roomId,
            @RequestBody RoomRequestDto roomRequestDto) {
        RoomResponseDto roomResponseDto = roomService.updateRoom(roomId, roomRequestDto);
        return new ResponseEntity<>(ApiResponse.successResponse(roomResponseDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a room",
            description = "Deletes the specified room and its associated inventory."
    )
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @Parameter(
                    description = "ID of the room",
                    example = "1"
            )@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }
}

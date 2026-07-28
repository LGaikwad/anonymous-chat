package com.example.chat.controller;

import com.example.chat.dto.MessageResponse;
import com.example.chat.entity.ChatRoom;
import com.example.chat.service.MessageService;
import com.example.chat.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;
    private final MessageService messageService;

    public RoomController(RoomService roomService, MessageService messageService) {
        this.roomService = roomService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createRoom() {
        ChatRoom room = roomService.createRoom();
        return ResponseEntity.ok(Map.of("roomCode", room.getRoomCode()));
    }

    @GetMapping("/{roomCode}")
    public ResponseEntity<Map<String, String>> getRoom(@PathVariable String roomCode) {
        roomService.requireRoom(roomCode);
        return ResponseEntity.ok(Map.of("roomCode", roomCode.toUpperCase()));
    }

    @GetMapping("/{roomCode}/messages")
    public List<MessageResponse> history(@PathVariable String roomCode) {
        return messageService.history(roomCode);
    }
}

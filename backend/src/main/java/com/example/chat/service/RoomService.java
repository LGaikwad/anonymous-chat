package com.example.chat.service;

import com.example.chat.entity.ChatRoom;
import com.example.chat.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class RoomService {
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();
    private final ChatRoomRepository repository;

    public RoomService(ChatRoomRepository repository) {
        this.repository = repository;
    }

    public ChatRoom createRoom() {
        String code;
        do {
            code = randomCode(6);
        } while (repository.existsByRoomCode(code));
        return repository.save(new ChatRoom(code));
    }

    public ChatRoom requireRoom(String code) {
        return repository.findByRoomCode(code.toUpperCase())
            .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    private String randomCode(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return result.toString();
    }
}

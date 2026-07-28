package com.example.chat.dto;

import com.example.chat.entity.ChatMessage;
import java.time.Instant;

public record MessageResponse(
    Long id,
    String roomCode,
    String userCode,
    String message,
    Instant createdAt
) {
    public static MessageResponse from(ChatMessage m) {
        return new MessageResponse(
            m.getId(),
            m.getRoom().getRoomCode(),
            m.getUserCode(),
            m.getMessage(),
            m.getCreatedAt()
        );
    }
}

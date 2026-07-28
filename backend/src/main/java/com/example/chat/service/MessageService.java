package com.example.chat.service;

import com.example.chat.dto.MessageResponse;
import com.example.chat.entity.ChatMessage;
import com.example.chat.entity.ChatRoom;
import com.example.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MessageService {
    private final ChatMessageRepository messageRepository;
    private final RoomService roomService;

    public MessageService(ChatMessageRepository messageRepository, RoomService roomService) {
        this.messageRepository = messageRepository;
        this.roomService = roomService;
    }

    public List<MessageResponse> history(String roomCode) {
        ChatRoom room = roomService.requireRoom(roomCode);
        List<ChatMessage> messages = messageRepository.findTop100ByRoomOrderByCreatedAtDesc(room);
        Collections.reverse(messages);
        return messages.stream().map(MessageResponse::from).toList();
    }

    public MessageResponse save(String roomCode, String userCode, String text) {
        if (userCode == null || !userCode.matches("USER-[0-9]{4}")) {
            throw new IllegalArgumentException("Invalid user code");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        if (text.length() > 500) {
            throw new IllegalArgumentException("Message is too long");
        }

        ChatRoom room = roomService.requireRoom(roomCode);
        ChatMessage saved = messageRepository.save(
            new ChatMessage(room, userCode, text.trim())
        );
        return MessageResponse.from(saved);
    }
}

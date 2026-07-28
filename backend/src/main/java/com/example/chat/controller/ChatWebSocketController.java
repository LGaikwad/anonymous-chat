package com.example.chat.controller;

import com.example.chat.dto.MessageResponse;
import com.example.chat.dto.SendMessageRequest;
import com.example.chat.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    private final MessageService messageService;

    public ChatWebSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat/{roomCode}")
    @SendTo("/topic/room/{roomCode}")
    public MessageResponse send(
        @DestinationVariable String roomCode,
        @Valid SendMessageRequest request
    ) {
        return messageService.save(
            roomCode,
            request.userCode(),
            request.message()
        );
    }
}

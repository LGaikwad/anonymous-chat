package com.example.chat.repository;

import com.example.chat.entity.ChatMessage;
import com.example.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop100ByRoomOrderByCreatedAtDesc(ChatRoom room);
}

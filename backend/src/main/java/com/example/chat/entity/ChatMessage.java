package com.example.chat.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "messages", indexes = @Index(name = "idx_message_room_time", columnList = "room_id, createdAt"))
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom room;

    @Column(nullable = false, length = 20)
    private String userCode;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private Instant createdAt;

    public ChatMessage() {}

    public ChatMessage(ChatRoom room, String userCode, String message) {
        this.room = room;
        this.userCode = userCode;
        this.message = message;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public ChatRoom getRoom() { return room; }
    public String getUserCode() { return userCode; }
    public String getMessage() { return message; }
    public Instant getCreatedAt() { return createdAt; }
}

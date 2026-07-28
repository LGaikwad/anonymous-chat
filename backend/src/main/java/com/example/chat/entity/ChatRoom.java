package com.example.chat.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "rooms", indexes = @Index(name = "idx_room_code", columnList = "roomCode", unique = true))
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String roomCode;

    @Column(nullable = false)
    private Instant createdAt;

    public ChatRoom() {}

    public ChatRoom(String roomCode) {
        this.roomCode = roomCode;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getRoomCode() { return roomCode; }
    public Instant getCreatedAt() { return createdAt; }
}

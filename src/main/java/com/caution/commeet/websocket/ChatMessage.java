package com.caution.commeet.websocket;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "message")
    private String message;

    @Column(name = "room_number")
    private Long roomNumber;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Builder
    public ChatMessage(Long userId, String message, String imageUrl, Long roomNumber) {
        this.userId = userId;
        this.message = message;
        this.roomNumber = roomNumber;
        this.imageUrl = imageUrl;
        this.createdTime = LocalDateTime.now();
    }
}

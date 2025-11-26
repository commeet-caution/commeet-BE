package com.caution.commeet.websocket;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_message")
@Getter
@NoArgsConstructor
public class ChatMessage extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "room_number")
    private Long roomNumber;

    @Column(name = "message")
    private String message;

    @Column(name = "image_url")
    private String imageUrl;

    @Builder
    public ChatMessage(Long userId, Long roomNumber, String message, String imageUrl) {
        this.userId = userId;
        this.roomNumber = roomNumber;
        this.message = message;
        this.imageUrl = imageUrl;
    }
}

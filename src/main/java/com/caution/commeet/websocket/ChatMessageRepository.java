package com.caution.commeet.websocket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 채팅방의 메시지 전체 조회
    List<ChatMessage> findByRoomNumber(Long roomNumber);

    // 특정 채팅방 메시지 전체 삭제
    void deleteByRoomNumber(Long roomNumber);
}

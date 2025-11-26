package com.caution.commeet.websocket;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 두 사용자의 채팅방 단일 조회 (정렬된 userId 기준)
    Optional<ChatRoom> findByUser_IdAndOther_Id(Long userId, Long otherId);

    // 특정 사용자가 속한 모든 채팅방 조회
    List<ChatRoom> findByUser_IdOrOther_Id(Long userId, Long otherId);

    // 방 번호로 단일 채팅방 조회
    Optional<ChatRoom> findByRoomNumber(Long roomNumber);
}


package com.caution.commeet.websocket;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    // 특정 채팅방의 메시지 전체 조회
    @Transactional
    public List<ChatMessageResponseDto> getMessagesByRoomNumber(Long roomNumber) {
        return chatMessageRepository.findByRoomNumber(roomNumber)
                .stream()
                .map(ChatMessageResponseDto::new)
                .toList();
    }

    // 메시지 저장
    @Transactional
    public void saveMessage(ChatMessageRequestDto requestDto) {
        ChatMessage chatMessage = requestDto.toEntity();
        chatMessageRepository.save(chatMessage);
    }

    // 특정 채팅방의 메시지 전체 삭제
    @Transactional
    public void deleteMessagesByRoomNumber(Long roomNumber) {
        chatMessageRepository.deleteByRoomNumber(roomNumber);
    }
}
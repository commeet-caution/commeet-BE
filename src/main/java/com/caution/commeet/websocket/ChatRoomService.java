package com.caution.commeet.websocket;

import com.caution.commeet.domain.CustomUserDetails;
import com.caution.commeet.domain.User;
import com.caution.commeet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    // 현재 사용자와 상대 사용자 간의 채팅방이 존재하면 반환, 없으면 새로 생성하여 반환
    @Transactional
    public ChatRoomWithMessagesDto getOrCreateRoom(User currentUser, Long otherUserId) {

        User otherUser = userService.findById(otherUserId);

        return findByUserPair(currentUser, otherUser)
                .map(room -> new ChatRoomWithMessagesDto(
                        room,
                        chatMessageService.getMessagesByRoomNumber(room.getRoomNumber())
                ))
                .orElseGet(() -> new ChatRoomWithMessagesDto(
                        createRoom(currentUser, otherUser),
                        Collections.emptyList()
                ));
    }

    // 두 사용자 ID를 정렬하여 일관성 있게 채팅방 생성
    @Transactional
    public ChatRoom createRoom(User user1, User user2) {
        Long firstId = Math.min(user1.getId(), user2.getId());
        Long secondId = Math.max(user1.getId(), user2.getId());

        User firstUser = userService.getReferenceById(firstId);
        User secondUser = userService.getReferenceById(secondId);

        return chatRoomRepository.save(
                ChatRoom.builder()
                        .user(firstUser)
                        .other(secondUser)
                        .build()
        );
    }

    // 두 사용자의 ID 조합으로 채팅방 단일 조회
    public Optional<ChatRoom> findByUserPair(User user1, User user2) {

        Long firstId = Math.min(user1.getId(), user2.getId());
        Long secondId = Math.max(user1.getId(), user2.getId());

        return chatRoomRepository.findByUser_IdAndOther_Id(firstId, secondId);
    }

    // 해당 사용자(userId)가 참여 중인 모든 채팅방 조회
    @Transactional(readOnly = true)
    public List<ChatRoomWithMessagesDto> getChatRoomsByUserId(Long userId) {

        return chatRoomRepository
                .findByUser_IdOrOther_Id(userId, userId)
                .stream()
                .map(room -> new ChatRoomWithMessagesDto(
                        room,
                        chatMessageService.getMessagesByRoomNumber(room.getRoomNumber())
                ))
                .toList();
    }

    // 채팅방 삭제 (채팅방 생성자 = userId)
    @Transactional
    public void deleteChatRoom(CustomUserDetails userDetails, Long roomNumber) {

        ChatRoom chatRoom = chatRoomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "채팅방을 찾을 수 없습니다."
                ));

        Long currentUserId = userDetails.getUser().getId();

        if (!chatRoom.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "이 채팅방을 삭제할 권한이 없습니다."
            );
        }

        chatMessageService.deleteMessagesByRoomNumber(roomNumber);
        chatRoomRepository.delete(chatRoom);
    }
}
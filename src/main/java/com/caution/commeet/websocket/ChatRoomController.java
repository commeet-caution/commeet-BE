package com.caution.commeet.websocket;

import com.caution.commeet.domain.CustomUserDetails;
import com.caution.commeet.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatting")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성 또는 기존 채팅방 반환
    @PostMapping("room")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatRoomWithMessagesDto> makeRoom(@RequestBody ChatRoomRequestDto requestDto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User currentUser = userDetails.getUser();

        ChatRoomWithMessagesDto roomInfo = chatRoomService.getOrCreateRoom(currentUser, requestDto.getOtherId());

        return ResponseEntity.ok(roomInfo);
    }

    // 사용자가 속한 채팅방 목록 조회
    @GetMapping("/rooms")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatRoomWithMessagesDto>> getUserChatRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {

        List<ChatRoomWithMessagesDto> chatRooms = chatRoomService.getChatRoomsByUserId(userDetails.getUser().getId());

        return ResponseEntity.ok(chatRooms);
    }

    //채팅방 삭제
    @DeleteMapping("/rooms/{roomNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteChatRoom(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long roomNumber) {

        chatRoomService.deleteChatRoom(userDetails, roomNumber);

        return ResponseEntity.ok("채팅방이 삭제되었습니다.");
    }
}

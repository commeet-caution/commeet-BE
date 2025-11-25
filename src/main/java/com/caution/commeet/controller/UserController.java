package com.caution.commeet.controller;

import com.caution.commeet.dto.user.MyInfoResponseDto;
import com.caution.commeet.dto.user.MyInfoUpdateRequestDto;
import com.caution.commeet.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 내 정보 조회 API
     * [GET] /api/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<MyInfoResponseDto> getMyInfo(){
        // 1. "지금 로그인한 사람"의 ID(loginId)를 꺼냅니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoginId = authentication.getName(); // SecurityConfig 설정상 loginId가 들어있습니다.

        // 2. 서비스 호출
        MyInfoResponseDto myInfo = userService.getMyInfo(currentLoginId);

        return ResponseEntity.ok(myInfo);
    }


    /**
     * 내 정보 수정 API
     * [PATCH] /api/users/me
     */
    @PatchMapping("/me")
    public ResponseEntity<Void> updateMyInfo(@RequestBody MyInfoUpdateRequestDto requestDto) {
        // 1. 로그인한 사용자 ID 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoginId = authentication.getName();

        // 2. 서비스 호출
        userService.updateMyInfo(currentLoginId, requestDto);

        return ResponseEntity.ok().build();
    }


    /**
     * 회원 탈퇴 API (탈퇴 후 즉시 로그아웃 처리)
     * [DELETE] /api/users/me
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        // 1. 로그인한 사용자 ID 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoginId = authentication.getName();

        // 2. 서비스 호출 (Soft Delete 수행)
        userService.deleteUser(currentLoginId);

        // 3.  강제 로그아웃 처리
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return ResponseEntity.ok().build();
    }
}

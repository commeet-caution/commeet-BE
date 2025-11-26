package com.caution.commeet.controller;

import com.caution.commeet.dto.AddUserRequest;
import com.caution.commeet.dto.LoginResponse;
import com.caution.commeet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody AddUserRequest request) {
        userService.register(request);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PostMapping("/login-success")
    public ResponseEntity<LoginResponse> loginSuccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Principal이 User 엔티티라고 가정
        var principal = (com.caution.commeet.domain.User) auth.getPrincipal();
        Long userId = principal.getId();

        // DB에서 사용자 조회
        com.caution.commeet.domain.User user = userService.findById(userId);

        // DTO로 매핑
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUniversity(user.getUniversity());
        response.setDepartment(user.getDepartment());
        response.setRole(user.getRole());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login-fail")
    public ResponseEntity<Map<String, String>> loginFail() {
        return ResponseEntity.ok(Map.of("message", "fail"));
    }

    @GetMapping("/session")
    public ResponseEntity<Object> getSessionUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getPrincipal());
    }

    @GetMapping("/logout-success")
    public ResponseEntity<Map<String, String>> logoutSuccess() {
        return ResponseEntity.ok(Map.of("message", "logout success"));
    }
}
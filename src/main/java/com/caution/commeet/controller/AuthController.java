package com.caution.commeet.controller;

import com.caution.commeet.dto.AddUserRequest;
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
    public ResponseEntity<Map<String, String>> loginSuccess() {
        return ResponseEntity.ok(Map.of("message", "success"));
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
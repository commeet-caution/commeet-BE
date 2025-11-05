package com.caution.commeet.controller;

import com.caution.commeet.dto.AddUserRequest;
import com.caution.commeet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody AddUserRequest request) {
        userService.register(request);
        return "success";
    }

    @PostMapping("/login-success")
    @ResponseBody
    public String loginSuccess() {
        return "success";
    }

    @PostMapping("/login-fail")
    @ResponseBody
    public String loginFail() {
        return "fail";
    }

    @GetMapping("/session")
    @ResponseBody
    public Object getSessionUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal(); // CustomUserDetails
    }

    @GetMapping("/logout-success")
    @ResponseBody
    public String logoutSuccess() {
        return "logout success";
    }
}

package com.caution.commeet.dto;

import com.caution.commeet.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String loginId;
    private String password;
    private UserRole userRole;
}

package com.caution.commeet.dto;

import com.caution.commeet.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Long Id;
    private String loginId;
    private String name;
    private String university;
    private String department;
    private UserRole role;
}

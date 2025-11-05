package com.caution.commeet.dto;

import lombok.Getter;
import com.caution.commeet.domain.UserRole;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddUserRequest {

    private String loginId;
    private String password;
    private String name;
    private String university;
    private String department;
    private UserRole role;
}

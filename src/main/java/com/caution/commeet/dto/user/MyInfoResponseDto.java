package com.caution.commeet.dto.user;

import com.caution.commeet.domain.Profile;
import com.caution.commeet.domain.User;
import com.caution.commeet.domain.UserRole;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MyInfoResponseDto {

    private Long id;
    private String loginId;
    private String name;
    private String university;
    private String department;
    private UserRole role;
    private String profileContent; //없으면 null


    public static MyInfoResponseDto of(User user, Profile profile) {
        return MyInfoResponseDto.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .university(user.getUniversity())
                .department(user.getDepartment())
                .role(user.getRole())
                .profileContent(profile != null ? profile.getContent() : null)
                .build();
    }

}

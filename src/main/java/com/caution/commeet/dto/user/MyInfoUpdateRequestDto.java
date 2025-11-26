package com.caution.commeet.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyInfoUpdateRequestDto {
    private String name;
    private String university;
    private String department;
    private String profileContent;
}

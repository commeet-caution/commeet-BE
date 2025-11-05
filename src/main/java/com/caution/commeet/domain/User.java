package com.caution.commeet.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String loginId; // 로그인용 ID

    @NotNull
    private String password; // 암호화된 비밀번호

    @NotNull
    private String name; // 사용자 이름

    private String university; // 소속 대학교
    private String department; // 학과명

    @Enumerated(EnumType.STRING)
    private UserRole role; // STUDENT, PROFESSOR, ADMIN 등

    @Builder
    public User(String loginId, String password, String name,
                String university, String department, UserRole role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.university = university;
        this.department = department;
        this.role = role;
    }

    public void updateProfile(String name, String university, String department) {
        this.name = name;
        this.university = university;
        this.department = department;
    }
}

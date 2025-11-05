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
    private String loginId;

    @NotNull
    private String password;

    @NotNull
    private String name;

    private String university;
    private String department;

    @Enumerated(EnumType.STRING)
    private UserRole role;

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


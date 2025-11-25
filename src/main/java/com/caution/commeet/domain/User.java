package com.caution.commeet.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP where user_id = ?")
@SQLRestriction("deleted_at IS NULL") //"같은 아이디로 재가입" 할 때 문제 발생, 일단 soft delete 구현을 위해 추가
public class User extends BaseTimeEntity{

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


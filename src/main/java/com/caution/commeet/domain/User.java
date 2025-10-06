package com.caution.commeet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP where user_id = ?")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    private String university;

    @NotNull
    private String department;

    @NotNull
    private String email;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private com.caution.commeet.domain.UserRole role;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Builder
    public User(String university, String department, String email, String password,
                String name, com.caution.commeet.domain.UserRole role, AuthProvider authProvider) {
        this.university = university;
        this.department = department;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        if (authProvider != null) this.authProvider = authProvider;
    }

    public User update(String name) {
        this.name = name;

        return this;
    }
}
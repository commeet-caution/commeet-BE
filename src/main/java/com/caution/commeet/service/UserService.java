package com.caution.commeet.service;

import com.caution.commeet.domain.User;
import com.caution.commeet.domain.UserRole;
import com.caution.commeet.dto.AddUserRequest;
import com.caution.commeet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입 (비밀번호 암호화 + 기본 권한 설정)
     */
    public Long register(AddUserRequest dto) {
        User user = User.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .university(dto.getUniversity())
                .department(dto.getDepartment())
                .role(dto.getRole() != null ? dto.getRole() : UserRole.ROLE_STUDENT)
                .build();

        return userRepository.save(user).getId();
    }

    /**
     * 로그인 (비밀번호 검증 포함)
     */
    public User login(String loginId, String password, UserRole role) {
        User user = userRepository.findByLoginIdAndRole(loginId, role)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID: " + id + " 에 해당하는 사용자가 없습니다."));
    }
}

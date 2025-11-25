package com.caution.commeet.service;

import com.caution.commeet.domain.Profile;
import com.caution.commeet.domain.User;
import com.caution.commeet.domain.UserRole;
import com.caution.commeet.dto.AddUserRequest;
import com.caution.commeet.dto.user.MyInfoResponseDto;
import com.caution.commeet.dto.user.MyInfoUpdateRequestDto;
import com.caution.commeet.repository.ProfileRepository;
import com.caution.commeet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

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

    /**
     * 로그인한 사용자의 상세 정보를 조회합니다.
     * @param loginId 인증된 사용자의 로그인 ID (예: prof_park)
     * @return 사용자 정보 + 프로필 정보 DTO
     */
    public MyInfoResponseDto getMyInfo(String loginId) {
        // 1. 사용자 조회
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 프로필 조회 (없을 수도 있음)
        Profile profile = profileRepository.findByUserId(user.getId())
                .orElse(null);

        // 3. DTO 변환 및 반환
        return MyInfoResponseDto.of(user, profile);
    }


    /**
     * 내 정보를 수정합니다.
     */
    @Transactional
    public void updateMyInfo(String loginId, MyInfoUpdateRequestDto requestDto) {
        // 1. 사용자 조회
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. User 기본 정보 수정 (이름, 학교, 학과)
        user.updateProfile(
                requestDto.getName(),
                requestDto.getUniversity(),
                requestDto.getDepartment()
        );

        // 3. Profile 소개글 수정 (프로필이 없으면 생성)
        Profile profile = profileRepository.findByUserId(user.getId())
                .orElse(null);

        if (profile == null) {
            // 프로필이 없으면 새로 생성해서 저장
            profile = Profile.builder()
                    .user(user)
                    .content(requestDto.getProfileContent())
                    .build();
            profileRepository.save(profile);
        } else {
            profile.updateContent(requestDto.getProfileContent());
        }


    }


    /**
     * 회원 탈퇴 (Soft Delete)
     */
    @Transactional
    public void deleteUser(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }



}

package com.caution.commeet.service;

import com.caution.commeet.domain.Profile;
import com.caution.commeet.domain.User;
import com.caution.commeet.dto.user.MyInfoResponseDto;
import com.caution.commeet.dto.user.MyInfoUpdateRequestDto;
import com.caution.commeet.repository.ProfileRepository;
import com.caution.commeet.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    // --- 1. 내 정보 조회 테스트 ---
    @Test
    @DisplayName("내 정보를 성공적으로 조회한다")
    void getMyInfo_Success() {
        // given
        String loginId = "test_user";
        User user = mock(User.class);
        Profile profile = mock(Profile.class);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(user.getId())).thenReturn(Optional.of(profile));

        // DTO 변환
        when(user.getLoginId()).thenReturn(loginId);
        when(user.getName()).thenReturn("홍길동");
        when(profile.getContent()).thenReturn("자기소개입니다.");

        // when
        MyInfoResponseDto result = userService.getMyInfo(loginId);

        // then
        assertNotNull(result);
        assertEquals(loginId, result.getLoginId());
        assertEquals("홍길동", result.getName());
        assertEquals("자기소개입니다.", result.getProfileContent());
    }

    // --- 2. 내 정보 수정 테스트 (프로필 생성 vs 수정) ---
    @Test
    @DisplayName("프로필이 없을 때, 내 정보를 수정하면 프로필이 새로 생성된다")
    void updateMyInfo_CreateProfile_Success() {
        // given
        String loginId = "test_user";
        MyInfoUpdateRequestDto requestDto = new MyInfoUpdateRequestDto();
        requestDto.setName("변경된 이름");
        requestDto.setProfileContent("새로운 소개");

        User user = mock(User.class);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        //프로필이 없다고 설정 (Empty)
        when(profileRepository.findByUserId(user.getId())).thenReturn(Optional.empty());

        // when
        userService.updateMyInfo(loginId, requestDto);

        // then
        // 1. User 정보 업데이트 메서드가 호출되었는지 검증
        verify(user).updateProfile(requestDto.getName(), requestDto.getUniversity(), requestDto.getDepartment());
        // 2. ProfileRepository의 save(생성)가 호출되었는지 검증
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("프로필이 이미 있을 때, 내 정보를 수정하면 내용만 변경된다")
    void updateMyInfo_UpdateProfile_Success() {
        // given
        String loginId = "test_user";
        MyInfoUpdateRequestDto requestDto = new MyInfoUpdateRequestDto();
        requestDto.setProfileContent("변경된 소개");

        User user = mock(User.class);
        Profile profile = mock(Profile.class);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(user.getId())).thenReturn(Optional.of(profile));

        // when
        userService.updateMyInfo(loginId, requestDto);

        // then
        verify(profile).updateContent(requestDto.getProfileContent());
        verify(profileRepository, never()).save(any(Profile.class));
    }

    // --- 3. 회원 탈퇴 테스트 ---
    @Test
    @DisplayName("회원 탈퇴를 성공적으로 수행한다")
    void deleteUser_Success() {
        // given
        String loginId = "test_user";
        User user = mock(User.class);

        when(userRepository.findByLoginId(loginId)).thenReturn(Optional.of(user));

        // when
        userService.deleteUser(loginId);

        // then
        verify(userRepository).delete(user);
    }
}
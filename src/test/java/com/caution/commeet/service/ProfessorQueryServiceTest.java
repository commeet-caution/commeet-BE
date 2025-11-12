package com.caution.commeet.service;

import com.caution.commeet.domain.AvailableSlot;
import com.caution.commeet.domain.Profile;
import com.caution.commeet.domain.User;
import com.caution.commeet.dto.AvailabilityResponse;
import com.caution.commeet.dto.professor.ProfessorDetailDto;
import com.caution.commeet.dto.user.ProfessorListDto;
import com.caution.commeet.repository.AvailabilityRepository;
import com.caution.commeet.repository.ProfileRepository;
import com.caution.commeet.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ProfessorQueryService에 대한 단위 테스트
 */

@ExtendWith(MockitoExtension.class)
class ProfessorQueryServiceTest {

    @InjectMocks //테스트 대상
    private  ProfessorQueryService professorQueryService;

    @Mock //가짜 객체
    private UserRepository userRepository;


    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AvailabilityService availabilityService;

    @Test
    @DisplayName("교수 목록 조회를 성공적으로 수행한다.")
    void searchProfessors_Success(){
        //given
        String department = "컴퓨터공학과";
        String name = "박교수";

        //가짜 User 객체 생성
        User professor1 = mock(User.class);

        //DTO 변환에 필요한 getter 메서드들의 행동을 정의
        when(professor1.getId()).thenReturn(1L);
        when(professor1.getName()).thenReturn("박교수");
        when(professor1.getDepartment()).thenReturn("컴퓨터공학과");
//        when(professor1.getEmail()).thenReturn("park@test.com");

        List<User> fakeProfessorList = List.of(professor1);

        //Repository가 특정 조건으로 호출되면, 위에서 만든 가짜 리스트를 반환하도록 설정
        when(userRepository.searchProfessors(department, name)).thenReturn(fakeProfessorList);

        //when - 실제 테스트 실행
        List<ProfessorListDto> result = professorQueryService.searchProfessors(department, name);

        //then - 결과 검증
        assertNotNull(result); //결과가 null이 아님
        assertEquals(1, result.size()); //결과 리스트의 크기 1

        ProfessorListDto dto = result.get(0);
        assertEquals(1L, dto.getProfessorId());
        assertEquals("박교수", dto.getName());
        assertEquals("컴퓨터공학과", dto.getDepartment());
        assertEquals("park@test.com", dto.getEmail());

        //userRepository의 searchProfessors 메서드가 정확히 1번 호출되었는지 검증
        verify(userRepository, times(1)).searchProfessors(department, name);
    }

    @Test
    @DisplayName("교수 상세 정보를 성공적으로 조회한다")
    void getProfessorDetails_Success() {
        // given - 테스트 준비
        long professorId = 1L;

        // 3. 가짜 엔티티 생성
        User professor = mock(User.class);
        Profile profile = mock(Profile.class);
        // [핵심 수정] DTO의 Builder를 사용하여 가짜 응답 객체 생성
        AvailabilityResponse slotResponse1 = AvailabilityResponse.builder()
                .id(201L)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .isBooked(false) // 예약 가능한 슬롯이므로 false
                .build();
        List<AvailabilityResponse> fakeSlotList = List.of(slotResponse1);

        // 4. 가짜 객체 행동 정의 (Repository 모킹)
        when(userRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(profileRepository.findByUserId(professorId)).thenReturn(Optional.of(profile));
        when(availabilityService.getProfessorAvailability(professorId))
                .thenReturn(fakeSlotList);

        // 5. DTO 변환에 필요한 getter 메서드 행동 정의
        when(professor.getId()).thenReturn(professorId);
        when(professor.getName()).thenReturn("박교수");
        when(professor.getDepartment()).thenReturn("컴퓨터공학과");
//        when(professor.getEmail()).thenReturn("park@test.com");
        when(profile.getContent()).thenReturn("AI 전문가입니다.");

        // when - 실제 테스트 실행
        ProfessorDetailDto resultDto = professorQueryService.getProfessorDetails(professorId);

        // then - 결과 검증
        assertNotNull(resultDto);
        assertEquals(professorId, resultDto.getProfessorId());
        assertEquals("박교수", resultDto.getName());
        assertEquals("AI 전문가입니다.", resultDto.getProfileContent());
        assertEquals(1, resultDto.getAvailableSlots().size()); // 슬롯 개수 확인

        // 6. 모든 Repository가 1번씩 호출되었는지 검증
        verify(userRepository, times(1)).findById(professorId);
        verify(profileRepository, times(1)).findByUserId(professorId);
        verify(availabilityService, times(1)).getProfessorAvailability(professorId);
    }





}
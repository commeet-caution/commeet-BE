package com.caution.commeet.service;

import com.caution.commeet.domain.Profile;
import com.caution.commeet.domain.User;
import com.caution.commeet.dto.AvailabilityResponse;
import com.caution.commeet.dto.professor.ProfessorDetailDto;
import com.caution.commeet.dto.user.ProfessorListDto;
import com.caution.commeet.repository.ProfileRepository;
import com.caution.commeet.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorQueryService {


    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final AvailabilityService availabilityService;

    /**
     * 조건(학과, 이름)에 맞는 교수 목록을 DTO 리스트로 조회합니다.
     *
     * @param department 검색할 학과 (null 가능)
     * @param name       검색할 이름 (null 가능)
     * @return 교수 목록 DTO 리스트
     */
    public List<ProfessorListDto> searchProfessors(String department, String name) {
        List<User> professors = userRepository.searchProfessors(department, name);

        return professors.stream()
                .map(ProfessorListDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 교수의 상세 정보를 조회합니다.
     * (기본 정보 + 프로필 + 예약 가능한 시간 목록)
     *
     * @param professorId 조회할 교수의 ID
     * @return 교수 상세 정보 DTO
     */
    public ProfessorDetailDto getProfessorDetails(Long professorId) {

        User professor = userRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("교수를 찾을 수 없습니다."));



        Profile profile = profileRepository.findByUserId(professorId).orElse(null);

        List<AvailabilityResponse> availableSlots =
                availabilityService.getProfessorAvailability(professorId);


        return new ProfessorDetailDto(professor, profile, availableSlots);
    }



}

package com.caution.commeet.service;

import com.caution.commeet.domain.User;
import com.caution.commeet.dto.user.ProfessorListDto;
import com.caution.commeet.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessorQueryService {

    private final UserRepositoryCustom userRepository;

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



}

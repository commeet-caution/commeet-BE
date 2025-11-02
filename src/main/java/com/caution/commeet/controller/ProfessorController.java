package com.caution.commeet.controller;

import com.caution.commeet.dto.user.ProfessorListDto;
import com.caution.commeet.service.ProfessorQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 교수 관련 API 요청을 처리하는 컨트롤러
 */

@RestController
@RequestMapping("/api/professors")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorQueryService professorQueryService;

    /**
     * 교수 목록을 검색 조건에 따라 조회하는 API
     * [GET] /api/professors?department=컴퓨터공학과&name=홍길동
     *
     * @param department 검색할 학과 (선택 사항)
     * @param name 검색할 이름 (선택 사항)
     * @return 200 OK 상태 코드와 함께 교수 목록 DTO 리스트를 반환
     */

    @GetMapping
    public ResponseEntity<List<ProfessorListDto>> searchProfessors(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String name) {

        List<ProfessorListDto> professors = professorQueryService.searchProfessors(department, name);
        return ResponseEntity.ok(professors);

    }

}

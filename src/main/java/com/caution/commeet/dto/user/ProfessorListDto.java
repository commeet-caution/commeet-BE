package com.caution.commeet.dto.user;

import com.caution.commeet.domain.User;
import lombok.Getter;

@Getter
public class ProfessorListDto {

    private final Long professorId;
    private final String name;
    private final String department;
    private final String email;

    //User 엔티티를 DTO로 변환
    public static ProfessorListDto from(User professor) {
        return new ProfessorListDto(
                professor.getId(),
                professor.getName(),
                professor.getDepartment(),
                professor.getLoginId()
        );
    }

    //생성자
    private ProfessorListDto(Long professorId, String name, String department, String email) {
        this.professorId = professorId;
        this.name = name;
        this.department = department;
        this.email = email;
    }


}

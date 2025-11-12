package com.caution.commeet.dto.professor;

import com.caution.commeet.domain.AvailableSlot;
import com.caution.commeet.domain.Profile;
import com.caution.commeet.domain.User;
import com.caution.commeet.dto.AvailabilityResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProfessorDetailDto {

    //1. 교수 기본 정보
    private final Long professorId;
    private final String name;
    private final String department;


    //2. 교수 프로필 정보
    private final String profileContent; // Profile 엔티티의 content

    //3. 예약 가능한 시간 목록
    private final List<SlotDto> availableSlots;


    // --- 내부 DTO ---
    @Getter
    private static class SlotDto{
        private final Long slotId;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;


        public SlotDto(AvailabilityResponse slotResponse) {
            this.slotId = slotResponse.getId();
            this.startTime = slotResponse.getStartTime();
            this.endTime = slotResponse.getEndTime();
        }
    }

    public ProfessorDetailDto(User professor, Profile profile, List<AvailabilityResponse> slots) {
        this.professorId = professor.getId();
        this.name = professor.getName();
        this.department = professor.getDepartment();


        // 프로필이 null일 수도 있으므로 방어 코드 작성
        this.profileContent = (profile != null) ? profile.getContent() : null;

        // AvailableSlot 리스트를 SlotDto 리스트로 변환
        this.availableSlots = slots.stream()
                .map(SlotDto::new) // .Sl(slotResponse -> new SlotDto(slotResponse))
                .collect(Collectors.toList());
    }
}

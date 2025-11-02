package com.caution.commeet.dto.availableslot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 교수가 상담 가능 시간을 등록할 때 사용하는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class AvailableSlotCreateRequestDto {

    private Long professorId;
    private List<SlotInfo> slots;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SlotInfo {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }
}
package com.caution.commeet.dto;

import com.caution.commeet.domain.AvailableSlot;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityResponse {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isBooked;

    public static AvailabilityResponse fromEntity(AvailableSlot slot) {
        return AvailabilityResponse.builder()
                .id(slot.getId())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .isBooked(slot.getIsBooked())
                .build();
    }
}

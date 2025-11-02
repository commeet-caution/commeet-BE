package com.caution.commeet.dto.appointment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppointmentCancelDto {
    private Long userId; // 학생 또는 교수 ID
    private String reason;
}
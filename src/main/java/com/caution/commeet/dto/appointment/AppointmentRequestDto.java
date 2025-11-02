package com.caution.commeet.dto.appointment;

import com.caution.commeet.domain.Topic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// 예약 신청 시 사용
@Getter
@NoArgsConstructor
@Setter
public class AppointmentRequestDto {
    private Long studentId;
    private Long slotId;
    private Topic topic;
    private String studentMessage; // 메시지를 받을 필드 추가
}
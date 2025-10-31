package com.caution.commeet.dto.appointment;

import com.caution.commeet.domain.Topic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 면담 예약 수정을 위한 요청 DTO
 * (시간, 주제, 메시지 변경 가능)
 */
@Getter
@Setter
@NoArgsConstructor
public class AppointmentUpdateRequestDto {

    private Long studentId;     // 본인 확인용
    private Long newSlotId;     // 변경할 새 시간 슬롯 ID
    private Topic topic;        // (선택 사항)
    private String studentMessage; // (선택 사항)

}
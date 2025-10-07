package com.caution.commeet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "available_slots")
@Getter
@NoArgsConstructor
public class AvailableSlot extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private User professor;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Boolean isBooked = false; // 기본값을 false로 설정


    // == 비즈니스 로직 메서드 == //
    /**
     * 이 슬롯을 예약 처리 상태(isBooked = true)로 변경
     * 이미 예약된 슬롯에 중복 요청이 오는 것을 방지
     */
    public void book() {
        if (this.isBooked) {
            throw new IllegalStateException("이미 예약 처리된 슬롯입니다.");
        }
        this.isBooked = true;
    }
}
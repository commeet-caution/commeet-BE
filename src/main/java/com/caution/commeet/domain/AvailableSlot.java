package com.caution.commeet.domain;

import com.caution.commeet.exception.SlotAlreadyBookedException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "available_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    @Column(nullable = false)
    private Boolean isBooked = false; // 기본값을 false로 설정


    // == 비즈니스 로직 메서드 == //
    /**
     * 이 슬롯을 예약 처리 상태(isBooked = true)로 변경
     * 이미 예약된 슬롯에 중복 요청이 오는 것을 방지
     */
    public void book() {
        if (this.isBooked) {
            throw new SlotAlreadyBookedException();
        }
        this.isBooked = true;
    }

    /**
     * 이 슬롯을 예약 처리 상태(isBooked = false)로 변경
     * 예약된 슬롯을 예약 가능한 슬롯으로 변경
     */
    public void makeAvailable() {
        this.isBooked = false;
    }

    // 아래 생성자를 클래스 내부에 추가해주세요.
    @Builder
    public AvailableSlot(User professor, LocalDateTime startTime, LocalDateTime endTime) {
        this.professor = professor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBooked = false; // isBooked의 기본값도 여기서 설정해주는 것이 안전합니다.
    }

}

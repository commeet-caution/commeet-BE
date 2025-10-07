package com.caution.commeet.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appointments")
@Getter
@NoArgsConstructor
public class Appointment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private User professor;

    @OneToOne(fetch = FetchType.LAZY) // 하나의 예약은 하나의 시간 슬롯과 1:1 매칭
    @JoinColumn(name = "slot_id", nullable = false)
    private AvailableSlot availableSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(columnDefinition = "TEXT") // 긴 텍스트를 위한 설정
    private String cancelReason;


    @Builder // 빌더 패턴을 적용할 생성자
    public Appointment(User student, User professor, AvailableSlot availableSlot, Topic topic, AppointmentStatus status) {
        this.student = student;
        this.professor = professor;
        this.availableSlot = availableSlot;
        this.topic = topic;
        this.status = status;
    }
    // == 비즈니스 로직 메서드 == //
    /**
     * 예약 상태를 '확정(CONFIRMED)'으로 변경합니다.
     * PENDING 상태일 때만 변경 가능합니다.
     */
    public void confirm() {
        if (this.status != AppointmentStatus.PENDING) {
            throw new IllegalStateException("대기 상태의 예약만 확정할 수 있습니다.");
        }
        this.status = AppointmentStatus.CONFIRMED;
    }


}
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
    private String cancelReason; //취소 이유

    @Column(columnDefinition = "TEXT")
    private String studentMessage; // 학생이 남긴 메시지 추가 필드, 면담 신청시



    @Builder // 빌더 패턴을 적용할 생성자
    public Appointment(User student, User professor, AvailableSlot availableSlot, Topic topic, AppointmentStatus status , String studentMessage) {
        this.student = student;
        this.professor = professor;
        this.availableSlot = availableSlot;
        this.topic = topic;
        this.status = status;
        this.studentMessage = studentMessage; // 필드 초기화
    }
    // == 비즈니스 로직 메서드 == //
    /**
     * 예약 상태를 '확정(CONFIRMED)'으로 변경합
     * PENDING 상태일 때만 변경 가능
     */
    public void confirm() {
        if (this.status != AppointmentStatus.PENDING) {
            throw new IllegalStateException("대기 상태의 예약만 확정할 수 있습니다.");
        }
        this.status = AppointmentStatus.CONFIRMED;
    }


    /**
     * 예약 상태를 '취소(CANCELED)'으로 변경
     *  COMPLETED 상태일 때는 변경 불가
     */
    public void cancel(String reason) {
        // 완료된 면담은 취소할 수 없도록 방어 로직 추가
        if (this.status == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("이미 완료된 면담은 취소할 수 없습니다.");
        }
        this.status = AppointmentStatus.CANCELED;
        this.cancelReason = reason;
    }


    /**
     * 예약 상태를 '완료(COMPLETED)'로 변경
     * 확정(CONFIRMED) 상태의 예약만 완료 처리 가능
     */
    public void complete() {
        if (this.status != AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("확정된 상태의 예약만 완료 처리할 수 있습니다.");
        }
        this.status = AppointmentStatus.COMPLETED;
    }





}
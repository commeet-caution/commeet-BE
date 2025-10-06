package com.caution.commeet.domain;

import jakarta.persistence.*;
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
}
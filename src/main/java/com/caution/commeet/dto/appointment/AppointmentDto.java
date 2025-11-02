package com.caution.commeet.dto.appointment;

import com.caution.commeet.domain.Appointment;
import com.caution.commeet.domain.AppointmentStatus;
import com.caution.commeet.domain.Topic;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class AppointmentDto {

    private final Long appointmentId;
    private final String studentName;
    private final String professorName;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Topic topic;
    private final AppointmentStatus status;

    //예약 목록 조회 시 화면에 보여줄 정보만 담은 DTO 클래스


    // 정적 팩토리 메서드: 엔티티를 DTO로 변환
    public static AppointmentDto from(Appointment appointment) {
        return new AppointmentDto(
                appointment.getId(),
                appointment.getStudent().getName(),
                appointment.getProfessor().getName(),
                appointment.getAvailableSlot().getStartTime(),
                appointment.getAvailableSlot().getEndTime(),
                appointment.getTopic(),
                appointment.getStatus()
        );
    }

    // 생성자
    private AppointmentDto(Long appointmentId, String studentName, String professorName, LocalDateTime startTime, LocalDateTime endTime, Topic topic, AppointmentStatus status) {
        this.appointmentId = appointmentId;
        this.studentName = studentName;
        this.professorName = professorName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.topic = topic;
        this.status = status;
    }
}
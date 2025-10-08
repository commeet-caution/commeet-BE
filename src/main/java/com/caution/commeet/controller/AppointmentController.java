package com.caution.commeet.controller;

import com.caution.commeet.domain.Appointment;
import com.caution.commeet.dto.appointment.AppointmentCancelDto;
import com.caution.commeet.dto.appointment.AppointmentConfirmDto;
import com.caution.commeet.dto.appointment.AppointmentDto;
import com.caution.commeet.dto.appointment.AppointmentRequestDto;
import com.caution.commeet.service.AppointmentQueryService;
import com.caution.commeet.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 예약 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentQueryService appointmentQueryService;
    private final ReservationService reservationService;

    /**
     * 특정 학생의 모든 예약 목록을 조회하는 API
     * [GET] /api/appointments/student/{studentId}
     *
     * @param studentId 조회할 학생의 ID
     * @return 200 OK 상태 코드와 함께 학생의 예약 DTO 리스트를 반환
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsForStudent(@PathVariable Long studentId) {
        // 3. Service를 호출하여 비즈니스 로직 수행
        List<AppointmentDto> appointments = appointmentQueryService.getAppointmentsForStudent(studentId);
        // 4. 결과를 ResponseEntity에 담아 반환
        return ResponseEntity.ok(appointments);
    }

    /**
     * 특정 교수의 모든 예약 목록을 조회하는 API
     * [GET] /api/appointments/professor/{professorId}
     *
     * @param professorId 조회할 교수의 ID
     * @return 200 OK 상태 코드와 함께 교수의 예약 DTO 리스트를 반환
     */
    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsForProfessor(@PathVariable Long professorId) {
        List<AppointmentDto> appointments = appointmentQueryService.getAppointmentsForProfessor(professorId);
        return ResponseEntity.ok(appointments);
    }

    // --- 데이터 변경 API ---

    /**
     * 학생이 면담 예약을 신청하는 API
     * [POST] /api/appointments
     *
     * @param requestDto 예약 신청에 필요한 정보 (studentId, slotId, topic)
     * @return 201 Created 상태 코드와 함께 생성된 예약의 DTO를 반환
     */
    @PostMapping
    public ResponseEntity<AppointmentDto> requestAppointment(@RequestBody AppointmentRequestDto requestDto) {
        // [수정] 여러 파라미터를 넘기는 대신, DTO 객체 자체를 그대로 전달합니다.
        Appointment newAppointment = reservationService.requestAppointment(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentDto.from(newAppointment));
    }

    /**
     * 교수가 면담 예약을 승인하는 API
     * [PATCH] /api/appointments/{appointmentId}/confirm
     *
     * @param appointmentId 승인할 예약의 ID
     * @param requestDto 승인을 요청한 교수의 ID (professorId)
     * @return 200 OK 상태 코드
     */
    @PatchMapping("/{appointmentId}/confirm")
    public ResponseEntity<Void> confirmAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentConfirmDto requestDto) {
        reservationService.confirmAppointment(requestDto.getProfessorId(), appointmentId);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자(학생/교수)가 면담 예약을 취소하는 API
     * [PATCH] /api/appointments/{appointmentId}/cancel
     *
     * @param appointmentId 취소할 예약의 ID
     * @param requestDto 취소를 요청한 사용자의 ID (userId) 및 취소 사유
     * @return 200 OK 상태 코드
     */
    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentCancelDto requestDto) {
        reservationService.cancelAppointment(requestDto.getUserId(), appointmentId, requestDto.getReason());
        return ResponseEntity.ok().build();
    }

}

package com.caution.commeet.controller;

import com.caution.commeet.domain.Appointment;
import com.caution.commeet.dto.appointment.*;
import com.caution.commeet.service.AppointmentQueryService;
import com.caution.commeet.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<AppointmentDto>> getAppointmentsForStudent(@PathVariable("studentId") Long studentId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName(); // 3. "로그인한 사람은 301인데요"


        if (!currentUserId.equals(studentId.toString())) {
            throw new SecurityException("자신의 예약 목록만 조회할 수 있습니다.");
        }


        // Service를 호출하여 비즈니스 로직 수행
        List<AppointmentDto> appointments = appointmentQueryService.getAppointmentsForStudent(studentId);
        // 결과를 ResponseEntity에 담아 반환
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
    public ResponseEntity<List<AppointmentDto>> getAppointmentsForProfessor(@PathVariable("professorId") Long professorId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = auth.getName(); // 3. "로그인한 사람은 301인데요"


        if (!currentUserId.equals(professorId.toString())) {
            throw new SecurityException("자신의 예약 목록만 조회할 수 있습니다.");
        }

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
        // 1. [수정] 서비스가 이제 DTO를 직접 반환
        AppointmentDto newAppointmentDto = reservationService.requestAppointment(requestDto);

        // 2. [수정] AppointmentDto.from() 호출 삭제
        return ResponseEntity.status(HttpStatus.CREATED).body(newAppointmentDto);
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
    public ResponseEntity<Void> confirmAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody AppointmentConfirmDto requestDto) {
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
    public ResponseEntity<Void> cancelAppointment(@PathVariable("appointmentId") Long appointmentId, @RequestBody AppointmentCancelDto requestDto) {
        reservationService.cancelAppointment(requestDto.getUserId(), appointmentId, requestDto.getReason());
        return ResponseEntity.ok().build();
    }

    /**
     *
     * [PATCH] /api/appointments/{appointmentId}/complete
     *
     * @param appointmentId 완료 처리할 예약의 ID
     * @param requestDto    완료를 요청한 교수의 ID (professorId)
     * @return 200 OK 상태 코드
     */
    @PatchMapping("/{appointmentId}/complete")
    public ResponseEntity<Void> completeAppointment(
            @PathVariable("appointmentId") Long appointmentId,
            @RequestBody AppointmentCompleteDto requestDto
    ) {
        reservationService.completeAppointment(requestDto.getProfessorId(), appointmentId);
        return ResponseEntity.ok().build();
    }



    /**
     * 학생이 면담 예약을 수정하는 API
     * [PATCH] /api/appointments/{appointmentId}
     *
     * @param appointmentId 수정할 예약의 ID
     * @param requestDto    수정할 내용(topic, message)과 본인 확인용 studentId
     * @return 200 OK 상태 코드
     */
    @PatchMapping("/{appointmentId}")
    public ResponseEntity<Void> updateAppointment(@PathVariable("appointmentId") Long appointmentId,
                                                  @RequestBody AppointmentUpdateRequestDto requestDto) {
        reservationService.updateAppointment(appointmentId, requestDto);
        return ResponseEntity.ok().build();
    }




}

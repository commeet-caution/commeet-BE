package com.caution.commeet.service;

import com.caution.commeet.domain.*; // 실제 프로젝트에 맞게 import 경로 설정 필요
import com.caution.commeet.dto.appointment.AppointmentRequestDto;
import com.caution.commeet.dto.availableslot.AvailableSlotCreateRequestDto;
import com.caution.commeet.repository.AppointmentRepository;
import com.caution.commeet.repository.AvailableSlotRepository;
import com.caution.commeet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final AvailableSlotRepository availableSlotRepository;
    private final AppointmentRepository appointmentRepository;

    private final EntityManager em;



    /**
     * 학생이 면담 예약을 '신청'하는 메서드
     *
     * @param requestDto 예약 신청에 필요한 정보(studentId, slotId, topic, studentMessage)를 담은 DTO
     * @return 생성된 Appointment 엔티티 (상태: PENDING)
     */
    @Transactional
    public Appointment requestAppointment(AppointmentRequestDto requestDto) { // 파라미터를 DTO로 받도록 변경
        User student = userRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        AvailableSlot slot = em.find(AvailableSlot.class, requestDto.getSlotId(), LockModeType.PESSIMISTIC_WRITE);
        // ... null 체크 ...

        slot.book();

        Appointment newAppointment = Appointment.builder()
                .student(student)
                .professor(slot.getProfessor())
                .availableSlot(slot)
                .topic(requestDto.getTopic())
                .status(AppointmentStatus.PENDING)
                .studentMessage(requestDto.getStudentMessage()) // DTO에서 받은 메시지를 엔티티에 설정
                .build();

        appointmentRepository.save(newAppointment);

        return newAppointment;
    }




    /**
     * 교수가 학생의 면담 신청을 '승인'하는 메서드
     * @param professorId 승인을 시도하는 교수의 ID
     * @param appointmentId 승인할 예약의 ID
     */
    @Transactional
    public void confirmAppointment(Long professorId, Long appointmentId) {
        // 1. 예약 정보 조회
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        // 2. 권한 확인: 예약을 승인하려는 교수가 실제 예약에 배정된 교수가 맞는지 확인
        if (!appointment.getProfessor().getId().equals(professorId)) {
            throw new SecurityException("예약을 승인할 권한이 없습니다."); // 예외 종류는 상황에 맞게 변경 가능
        }

        // 3. 상태 확인 및 변경: Appointment 엔티티에 상태 변경 로직 위임
        // (Appointment 엔티티에 confirm() 메서드가 구현되어 있다고 가정)
        appointment.confirm();

        // @Transactional에 의해 메서드가 끝나면 변경된 내용이 자동으로 DB에 반영 (Dirty Checking)
    }


    /**
     * 학생 또는 교수가 면담 예약을 '취소'하는 메서드
     * @param userId 취소를 요청한 사용자(학생 또는 교수)의 ID
     * @param appointmentId 취소할 예약의 ID
     * @param cancelReason 취소 사유
     */
    @Transactional
    public void cancelAppointment(Long userId, Long appointmentId, String cancelReason) {
        // 1. 예약 정보와 관련된 슬롯 정보를 함께 조회 (fetch join)
        Appointment appointment = appointmentRepository.findByIdWithSlot(appointmentId) // 이 메서드는 새로 추가해야 합니다.
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        // 2. 권한 확인: 학생 본인이거나, 담당 교수가 맞는지 확인
        boolean isStudent = appointment.getStudent().getId().equals(userId);
        boolean isProfessor = appointment.getProfessor().getId().equals(userId);

        if (!isStudent && !isProfessor) {
            throw new SecurityException("예약을 취소할 권한이 없습니다.");
        }

        // 3. 예약 취소 처리 (Appointment 엔티티에 로직 위임)
        appointment.cancel(cancelReason);

        // 4. 연결된 슬롯을 다시 예약 가능한 상태로 변경
        appointment.getAvailableSlot().makeAvailable();

        // @Transactional에 의해 모든 변경사항이 자동으로 DB에 반영됩니다.
    }


    /**
     * 교수가 진행한 면담을 '완료(COMPLETED)' 상태로 변경
     * 확정된(CONFIRMED) 상태의 예약만 완료 처리 가능
     *
     * @param professorId 완료 처리를 요청한 교수의 ID
     * @param appointmentId 완료 처리할 예약의 ID
     */
    @Transactional
    public void completeAppointment(Long professorId, Long appointmentId) {
        // 1. 예약 정보 조회
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약을 찾을 수 없습니다."));

        // 2. 권한 확인: 이 예약을 완료 처리하려는 교수가 실제 담당 교수가 맞는지 확인
        if (!appointment.getProfessor().getId().equals(professorId)) {
            throw new SecurityException("면담을 완료 처리할 권한이 없습니다.");
        }

        // 3. 상태 변경 로직을 Appointment 엔티티에 위임
        appointment.complete();
    }


    // ReservationService.java 클래스 안에 이어서 추가

    /**
     * 교수가 여러 개의 상담 가능 시간을 등록합니다.
     *
     * @param requestDto 등록할 교수의 ID와 시간 정보(시작/종료) 리스트를 담은 DTO
     */
    @Transactional
    public void createAvailableSlots(AvailableSlotCreateRequestDto requestDto) {
        // 1. 교수 엔티티를 조회
        User professor = userRepository.findById(requestDto.getProfessorId())
                .orElseThrow(() -> new IllegalArgumentException("교수를 찾을 수 없습니다."));

        // 2. DTO로부터 시간 정보 리스트를 가져와 AvailableSlot 엔티티 리스트로 변환
        List<AvailableSlot> newSlots = requestDto.getSlots().stream()
                .map(slotInfo -> new AvailableSlot( // AvailableSlot 엔티티에 적절한 생성자 필요
                        professor,
                        slotInfo.getStartTime(),
                        slotInfo.getEndTime()
                ))
                .collect(Collectors.toList());

        // 3. 생성된 모든 AvailableSlot 엔티티를 한 번에 저장합니다.
        availableSlotRepository.saveAll(newSlots);
    }


}
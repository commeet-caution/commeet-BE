package com.caution.commeet.service;

import com.caution.commeet.domain.*; // 실제 프로젝트에 맞게 import 경로 설정 필요
import com.caution.commeet.repository.AppointmentRepository;
import com.caution.commeet.repository.AvailableSlotRepository;
import com.caution.commeet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final AvailableSlotRepository availableSlotRepository;
    private final AppointmentRepository appointmentRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * 학생이 면담 예약을 '신청'하는 메서드
     * @param studentId 학생 ID
     * @param topic 면담 주제 Enum
     * @param slotId 예약할 시간 슬롯 ID
     * @return 생성된 Appointment 엔티티 (상태: PENDING)
     */
    @Transactional
    public Appointment requestAppointment(Long studentId, Topic topic, Long slotId) {
        // 1. 학생 엔티티 조회
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        // 2. 동시성 제어를 위해 비관적 락을 걸고 AvailableSlot 조회
        AvailableSlot slot = em.find(AvailableSlot.class, slotId, LockModeType.PESSIMISTIC_WRITE);
        if (slot == null) {
            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다.");
        }

        // 3. 슬롯의 예약 가능 여부 확인 후, 예약 처리 상태로 변경
        // (AvailableSlot 엔티티에 book() 메서드가 구현되어 있다고 가정)
        slot.book();

        // 4. Appointment 엔티티 생성. 초기 상태는 'PENDING'
        Appointment newAppointment = Appointment.builder()
                .student(student)
                .professor(slot.getProfessor())
                .availableSlot(slot)
                .topic(topic)
                .status(AppointmentStatus.PENDING) // [정책 반영] 초기 상태를 PENDING으로 설정
                .build();

        // 5. 생성된 예약 신청 정보를 저장
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
}
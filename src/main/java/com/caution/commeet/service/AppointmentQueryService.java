package com.caution.commeet.service;

import com.caution.commeet.domain.Appointment;
import com.caution.commeet.domain.User;
import com.caution.commeet.dto.appointment.AppointmentDto;
import com.caution.commeet.repository.AppointmentRepository;
import com.caution.commeet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 1. 조회 전용 트랜잭션 설정
public class AppointmentQueryService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    /**
     * 특정 학생의 모든 예약 목록을 DTO 리스트로 조회
     *
     * @param studentId 조회할 학생의 ID
     * @return 해당 학생의 예약 정보가 담긴 DTO 리스트
     */
    public List<AppointmentDto> getAppointmentsForStudent(Long studentId) {
        // 2. 학생 엔티티를 찾습니다.
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        // 3. 해당 학생의 모든 Appointment 엔티티를 조회합니다.
        List<Appointment> appointments = appointmentRepository.findByStudent(student);

        // 4. 조회된 엔티티 리스트를 DTO 리스트로 변환하여 반환합니다.
        return appointments.stream()
                .map(AppointmentDto::from) // .map(appointment -> AppointmentDto.from(appointment)) 와 동일
                .collect(Collectors.toList());
    }

    /**
     * 특정 교수의 모든 예약 목록을 DTO 리스트로 조회
     *
     * @param professorId 조회할 교수의 ID
     * @return 해당 교수의 예약 정보가 담긴 DTO 리스트
     */
    public List<AppointmentDto> getAppointmentsForProfessor(Long professorId) {
        User professor = userRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("교수를 찾을 수 없습니다."));

        List<Appointment> appointments = appointmentRepository.findByProfessor(professor);

        return appointments.stream()
                .map(AppointmentDto::from)
                .collect(Collectors.toList());
    }
}

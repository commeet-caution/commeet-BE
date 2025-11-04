package com.caution.commeet.repository;

import com.caution.commeet.domain.Appointment;
import com.caution.commeet.domain.AvailableSlot;
import com.caution.commeet.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment , Long> , AppointmentRepositoryCustom {

    /**
     * 특정 학생의 모든 예약을 조회
     * @param student 조회할 학생(User) 엔티티
     * @return List<Appointment> 조회된 예약 리스트
     */
    List<Appointment> findByStudent(User student);

    /**
     * 특정 교수의 모든 예약을 조회
     * @param professor 조회할 교수(User) 엔티티
     * @return List<Appointment> 조회된 예약 리스트
     */
    List<Appointment> findByProfessor(User professor);

    /**
     * 특정 AvailableSlot에 연결된 예약이 있는지 확인
     * (중복 예약을 방지하기 위한 핵심 기능)
     * @param availableSlot 확인할 AvailableSlot 엔티티
     * @return boolean 예약이 존재하면 true, 없으면 false
     */
    boolean existsByAvailableSlot(AvailableSlot availableSlot);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Appointment> findWithLockById(Long id);





}

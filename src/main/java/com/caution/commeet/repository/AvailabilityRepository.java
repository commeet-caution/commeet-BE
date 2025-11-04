package com.caution.commeet.repository;

import com.caution.commeet.domain.AvailableSlot;
import com.caution.commeet.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailableSlot, Long> {
    // 교수별 모든 가능한 슬롯 조회
    List<AvailableSlot> findByProfessor(User professor);

    // 교수별 예약되지 않은 슬롯 조회
    List<AvailableSlot> findByProfessorAndIsBookedFalse(User professor);


    /**
     * 특정 교수의 특정 기간 내 예약 가능 시간을 조회
     * (학생이 예약 화면을 볼 때 사용될 수 있습니다)
     * @param professor 조회할 교수(User) 엔티티
     * @param startTime 조회 시작 시간
     * @param endTime 조회 종료 시간
     * @return List<AvailableSlot> 조회된 슬롯 리스트
     */
    List<AvailableSlot> findByProfessorAndIsBookedFalseAndStartTimeBetween(
            User professor, LocalDateTime startTime, LocalDateTime endTime);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AvailableSlot> findWithLockById(Long id);
}
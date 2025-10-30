package com.caution.commeet.repository;

import com.caution.commeet.domain.AvailableSlot;
import com.caution.commeet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailableSlot, Long> {
    // 교수별 모든 가능한 슬롯 조회
    List<AvailableSlot> findByProfessor(User professor);

    // 교수별 예약되지 않은 슬롯 조회
    List<AvailableSlot> findByProfessorAndIsBookedFalse(User professor);
}
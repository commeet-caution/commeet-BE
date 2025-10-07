package com.caution.commeet.repository;

import com.caution.commeet.domain.AvailableSlot;
import com.caution.commeet.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AvailableSlotRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 예약 가능 시간을 데이터베이스에 저장
     * @param slot 저장할 AvailableSlot 엔티티
     */
    public void save(AvailableSlot slot) {
        em.persist(slot);
    }

    /**
     * 고유 ID로 예약 가능 시간을 조회
     * @param id 조회할 슬롯의 ID
     * @return Optional<AvailableSlot> 조회된 슬롯
     */
    public Optional<AvailableSlot> findById(Long id) {
        AvailableSlot slot = em.find(AvailableSlot.class, id);
        return Optional.ofNullable(slot);
    }

    /**
     * 특정 교수가 등록한 모든 예약 가능 시간을 조회
     * @param professor 조회할 교수(User) 엔티티
     * @return List<AvailableSlot> 조회된 슬롯 리스트
     */
    public List<AvailableSlot> findByProfessor(User professor) {
        return em.createQuery("SELECT a FROM AvailableSlot a WHERE a.professor = :professor", AvailableSlot.class)
                .setParameter("professor", professor)
                .getResultList();
    }

    /**
     * 특정 교수의 특정 기간 내 예약 가능 시간을 조회
     * (학생이 예약 화면을 볼 때 사용될 수 있습니다)
     * @param professor 조회할 교수(User) 엔티티
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return List<AvailableSlot> 조회된 슬롯 리스트
     */
    public List<AvailableSlot> findByProfessorAndStartTimeBetween(User professor, LocalDateTime start, LocalDateTime end) {
        return em.createQuery("SELECT a FROM AvailableSlot a WHERE a.professor = :professor AND a.startTime BETWEEN :start AND :end", AvailableSlot.class)
                .setParameter("professor", professor)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}

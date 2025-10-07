package com.caution.commeet.repository;

import com.caution.commeet.domain.Appointment;
import com.caution.commeet.domain.Memo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 메모를 데이터베이스에 저장
     * @param memo 저장할 Memo 엔티티
     */
    public void save(Memo memo) {
        em.persist(memo);
    }

    /**
     * 고유 ID로 메모를 조회
     * @param id 조회할 메모의 ID
     * @return Optional<Memo> 조회된 메모
     */
    public Optional<Memo> findById(Long id) {
        Memo memo = em.find(Memo.class, id);
        return Optional.ofNullable(memo);
    }

    /**
     * 특정 예약(Appointment)에 속한 모든 메모를 조회
     * @param appointment 메모를 조회할 Appointment 엔티티
     * @return List<Memo> 조회된 메모 리스트
     */
    public Optional<Memo> findByAppointment(Appointment appointment) {
        try {
            Memo memo = em.createQuery("SELECT m FROM Memo m WHERE m.appointment = :appointment", Memo.class)
                    .setParameter("appointment", appointment)
                    .getSingleResult(); // 결과가 하나만 있음을 기대
            return Optional.of(memo);
        } catch (NoResultException e) {
            return Optional.empty(); // 결과가 없으면 비어있는 Optional 반환
        }
    }
}
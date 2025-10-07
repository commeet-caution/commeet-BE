package com.caution.commeet.repository;

import com.caution.commeet.domain.Appointment;
import com.caution.commeet.domain.AvailableSlot;
import com.caution.commeet.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 확정된 예약을 데이터베이스에 저장합
     * @param appointment 저장할 Appointment 엔티티
     */
    public void save(Appointment appointment) {
        em.persist(appointment);
    }

    /**
     * 고유 ID로 예약을 조회합니다.
     * @param id 조회할 예약의 ID
     * @return Optional<Appointment> 조회된 예약 정보
     */
    public Optional<Appointment> findById(Long id) {
        Appointment appointment = em.find(Appointment.class, id);
        return Optional.ofNullable(appointment);
    }

    /**
     * 특정 학생의 모든 예약을 조회
     * @param student 조회할 학생(User) 엔티티
     * @return List<Appointment> 조회된 예약 리스트
     */
    public List<Appointment> findByStudent(User student) {
        return em.createQuery("SELECT a FROM Appointment a WHERE a.student = :student", Appointment.class)
                .setParameter("student", student)
                .getResultList();
    }

    /**
     * 특정 교수의 모든 예약을 조회
     * @param professor 조회할 교수(User) 엔티티
     * @return List<Appointment> 조회된 예약 리스트
     */
    public List<Appointment> findByProfessor(User professor) {
        return em.createQuery("SELECT a FROM Appointment a WHERE a.professor = :professor", Appointment.class)
                .setParameter("professor", professor)
                .getResultList();
    }

    /**
     * 특정 AvailableSlot에 연결된 예약이 있는지 확인
     * (중복 예약을 방지하기 위한 핵심 기능)
     * @param availableSlot 확인할 AvailableSlot 엔티티
     * @return boolean 예약이 존재하면 true, 없으면 false
     */
    public boolean existsByAvailableSlot(AvailableSlot availableSlot) {
        // COUNT 쿼리를 사용하여 해당 슬롯의 예약 개수를 셉니다.
        Long count = em.createQuery("SELECT COUNT(a) FROM Appointment a WHERE a.availableSlot = :slot", Long.class)
                .setParameter("slot", availableSlot)
                .getSingleResult();
        return count > 0;
    }



    public Optional<Appointment> findByIdWithSlot(Long appointmentId) {
        // JPQL을 사용하여 Appointment를 조회할 때, 연관된 AvailableSlot도 함께 즉시
        List<Appointment> result = em.createQuery(
                        "SELECT a FROM Appointment a JOIN FETCH a.availableSlot WHERE a.id = :id", Appointment.class)
                .setParameter("id", appointmentId)
                .getResultList();
        return result.stream().findFirst();
    }
}

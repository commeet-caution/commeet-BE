package com.caution.commeet.repository;

import com.caution.commeet.domain.Appointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;




@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements  AppointmentRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;


    /**
     * 예약(Appointment) ID를 사용하여 예약을 조회
     * 이때, 연관된 예약 가능 시간(AvailableSlot) 엔티티를 'fetch join'을 통해 함께 조회
     * @param appointmentId 조회할 예약의 ID
     * @return 조회된 Appointment 엔티티를 포함하는 Optional 객체. 연관된 AvailableSlot도 로딩된 상태
     * @apiNote 'fetch join'을 사용하는 이유:
     * 이 메서드 호출 후 appointment.getAvailableSlot()을 사용할 때,
     * 별도의 추가 쿼리(N+1 문제) 없이 이미 로딩된 AvailableSlot 엔티티를 바로 사용
     */
    @Override
    public Optional<Appointment> findByIdWithSlot(Long appointmentId) {
        List<Appointment> result = em.createQuery(
                        "SELECT a FROM Appointment a JOIN FETCH a.availableSlot WHERE a.id = :id", Appointment.class)
                .setParameter("id", appointmentId)
                .getResultList();

        return result.stream().findFirst();
    }
}

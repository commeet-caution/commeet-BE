package com.caution.commeet.repository;

import com.caution.commeet.domain.Appointment;
import java.util.Optional;

public interface AppointmentRepositoryCustom {

    /**
     * 예약 ID로 예약을 조회할 때, 연관된 AvailableSlot을 fetch join으로 함께 로딩합니다.
     */
    Optional<Appointment> findByIdWithSlot(Long appointmentId);
}

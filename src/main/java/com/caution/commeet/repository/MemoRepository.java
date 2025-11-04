package com.caution.commeet.repository;

import com.caution.commeet.domain.Appointment;
import com.caution.commeet.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo , Long> {

    Optional<Memo> findByAppointment(Appointment appointment);
}

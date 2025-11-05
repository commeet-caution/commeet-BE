package com.caution.commeet.repository;

import com.caution.commeet.domain.User;
import com.caution.commeet.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByLoginIdAndRole(String loginId, UserRole role);
}

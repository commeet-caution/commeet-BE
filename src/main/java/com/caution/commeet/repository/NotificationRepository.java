package com.caution.commeet.repository;

import com.caution.commeet.domain.Notification;
import com.caution.commeet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification , Long> {

    /**
     * 특정 사용자(수신자)의 모든 알림을 최신순으로 조회
     * @param user 알림을 조회할 User 엔티티
     * @return List<Notification> 조회된 알림 리스트
     */
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}

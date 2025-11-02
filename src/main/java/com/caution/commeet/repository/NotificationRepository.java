package com.caution.commeet.repository;

import com.caution.commeet.domain.Notification;
import com.caution.commeet.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 알림을 데이터베이스에 저장
     * @param notification 저장할 Notification 엔티티
     */
    public void save(Notification notification) {
        em.persist(notification);
    }

    /**
     * 고유 ID로 알림을 조회
     * @param id 조회할 알림의 ID
     * @return Optional<Notification> 조회된 알림
     */
    public Optional<Notification> findById(Long id) {
        Notification notification = em.find(Notification.class, id);
        return Optional.ofNullable(notification);
    }

    /**
     * 특정 사용자(수신자)의 모든 알림을 최신순으로 조회
     * @param user 알림을 조회할 User 엔티티
     * @return List<Notification> 조회된 알림 리스트
     */
    public List<Notification> findByUser(User user) {
        // JPQL을 사용하여 특정 사용자의 모든 알림을 조회
        // ORDER BY n.createdAt DESC 구문을 추가하여 최신 알림이 맨 위에 오도록 정렬
        return em.createQuery("SELECT n FROM Notification n WHERE n.user = :user ORDER BY n.createdAt DESC", Notification.class)
                .setParameter("user", user)
                .getResultList();
    }
}

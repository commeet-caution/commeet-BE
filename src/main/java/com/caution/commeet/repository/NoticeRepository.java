package com.caution.commeet.repository;

import com.caution.commeet.domain.Notice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class NoticeRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 공지사항을 데이터베이스에 저장
     * @param notice 저장할 Notice 엔티티
     */
    public void save(Notice notice) {
        em.persist(notice);
    }

    /**
     * 고유 ID로 공지사항을 조회
     * @param id 조회할 공지사항의 ID
     * @return Optional<Notice> 조회된 공지사항
     */
    public Optional<Notice> findById(Long id) {
        Notice notice = em.find(Notice.class, id);
        return Optional.ofNullable(notice);
    }

    /**
     * 모든 공지사항을 최신순으로 조회
     * @return List<Notice> 조회된 공지사항 리스트
     */
    public List<Notice> findAll() {
        // WHERE 조건 없이 모든 Notice 엔티티를 조회합니다.
        // ORDER BY n.createdAt DESC 구문을 추가하여 최신 공지가 맨 위에 오도록 정렬합니다.
        return em.createQuery("SELECT n FROM Notice n ORDER BY n.createdAt DESC", Notice.class)
                .getResultList();
    }
}

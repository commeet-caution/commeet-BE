package com.caution.commeet.repository;

import com.caution.commeet.domain.Profile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class ProfileRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 프로필을 데이터베이스에 저장
     * @param profile 저장할 Profile 엔티티
     */
    public void save(Profile profile) {
        em.persist(profile);
    }

    /**
     * 프로필의 고유 ID로 프로필을 조회
     * @param id 조회할 프로필의 ID
     * @return Optional<Profile> 조회된 프로필
     */
    public Optional<Profile> findById(Long id) {
        Profile profile = em.find(Profile.class, id);
        return Optional.ofNullable(profile);
    }

    /**
     * 사용자(User) ID로 프로필을 조회
     * @param userId 조회할 사용자의 ID
     * @return Optional<Profile> 조회된 프로필
     */
    public Optional<Profile> findByUserId(Long userId) {
        try {
            Profile profile = em.createQuery("SELECT p FROM Profile p WHERE p.user.id = :userId", Profile.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(profile);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
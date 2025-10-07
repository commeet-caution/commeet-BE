package com.caution.commeet.repository;

import com.caution.commeet.domain.User;
import com.caution.commeet.domain.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext // EntityManager를 주입
    private EntityManager em;

    /**
     * 사용자를 데이터베이스에 저장
     * @param user 저장할 User 엔티티
     */
    public void save(User user) {
        em.persist(user);
    }

    /**
     * 사용자의 고유 ID로 사용자를 조회
     * @param id 조회할 사용자의 ID
     * @return Optional<User> 조회된 사용자. 없으면 빈 Optional 객체.
     */
    public Optional<User> findById(Long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    /**
     * 사용자의 이메일로 사용자를 조회
     * @param email 조회할 사용자의 이메일
     * @return Optional<User> 조회된 사용자. 없으면 빈 Optional 객체.
     */
    public Optional<User> findByEmail(String email) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * 동적 검색 조건으로 교수를 조회
     * @param department 학과 (선택)
     * @param name 이름 (선택)
     * @return List<User> 조건에 맞는 교수 리스트
     */
    public  List<User> searchProfessors(String department, String name) {

        StringBuilder jpql = new StringBuilder("SELECT u FROM User u WHERE u.role = :role");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("role", UserRole.PROFESSOR);

        if (department != null && !department.isBlank()) {
            jpql.append(" AND u.department = :department");
            parameters.put("department", department);
        }

        if (name != null && !name.isBlank()) {
            jpql.append(" AND u.name LIKE :name");
            parameters.put("name", "%" + name + "%");
        }

        TypedQuery<User> query = em.createQuery(jpql.toString(), User.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }
}

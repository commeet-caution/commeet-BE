package com.caution.commeet.repository;

import com.caution.commeet.domain.User;
import com.caution.commeet.domain.UserRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class UserRepositoryImpl implements  UserRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<User> searchProfessors(String department, String name) {
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

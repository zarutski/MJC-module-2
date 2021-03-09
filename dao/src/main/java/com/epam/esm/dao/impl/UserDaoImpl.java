package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.entity.Role;
import com.epam.esm.domain.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class UserDaoImpl extends CommonOperationDao<User> implements UserDao {

    private static final String ATTRIBUTE_LOGIN = "login";

    public UserDaoImpl(EntityManager entityManager) {
        super(entityManager, User.class);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(criteriaBuilder.equal(root.get(ATTRIBUTE_LOGIN), login));
        return entityManager.createQuery(query).getResultStream().findFirst();
    }

    @Override
    public Optional<User> create(User user) {
        Role role = user.getRole();
        entityManager.merge(role);
        entityManager.persist(user);
        return Optional.of(user);
    }
}
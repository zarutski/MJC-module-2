package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dao.util.ParameterValue.INT_VALUE_OFFSET;

@Repository
public class UserDaoImpl implements UserDao {

    private static final String SELECT_USER_ID_WITH_ORDERS_HIGHEST_COST = "select orders.userId from Order orders " +
            "group by orders.userId order by sum(orders.cost) desc";
    private static final int VALUE_MAX_RESULT = 1;

    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> readById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public List<User> readAll(int page, int size) {
        CriteriaQuery<User> query = entityManager.getCriteriaBuilder().createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        int startPosition = (page - INT_VALUE_OFFSET) * size;
        return entityManager.createQuery(query)
                .setFirstResult(startPosition)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Long getEntitiesCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(User.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Long getUserIdWithOrdersHighestCost() {
        return entityManager.createQuery(SELECT_USER_ID_WITH_ORDERS_HIGHEST_COST, Long.class)
                .setMaxResults(VALUE_MAX_RESULT).getSingleResult();
    }
}
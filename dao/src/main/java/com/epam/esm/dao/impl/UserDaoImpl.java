package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserDaoImpl extends CommonOperationDao<User> implements UserDao {

    private static final String SELECT_USER_ID_WITH_ORDERS_HIGHEST_COST = "select orders.userId from Order orders " +
            "group by orders.userId order by sum(orders.cost) desc";
    private static final int VALUE_MAX_RESULT = 1;

    public UserDaoImpl(EntityManager entityManager) {
        super(entityManager, User.class);
        this.entityManager = entityManager;
    }

    @Override
    public Long getUserIdWithOrdersHighestCost() {
        return entityManager.createQuery(SELECT_USER_ID_WITH_ORDERS_HIGHEST_COST, Long.class)
                .setMaxResults(VALUE_MAX_RESULT).getSingleResult();
    }
}
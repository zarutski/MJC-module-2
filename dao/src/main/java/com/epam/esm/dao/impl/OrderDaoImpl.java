package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.exception.EntityNotInDBException;
import com.epam.esm.domain.entity.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dao.util.ParameterValue.INT_VALUE_OFFSET;

@Repository
public class OrderDaoImpl implements OrderDao {

    EntityManager entityManager;

    private static final String ATTRIBUTE_USER_ID = "userId";

    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Order> readAll(int page, int size) {
        CriteriaQuery<Order> query = entityManager.getCriteriaBuilder().createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        return entityManager.createQuery(query)
                .setFirstResult(getStartPosition(page, size))
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Optional<Order> readById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public List<Order> readOrdersByUserId(Long userId, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root).where(criteriaBuilder.equal(root.get(ATTRIBUTE_USER_ID), userId));
        List<Order> orderList = entityManager.createQuery(query)
                .setFirstResult(getStartPosition(page, size))
                .setMaxResults(size)
                .getResultList();
        if (orderList.isEmpty()) {
            throw new EntityNotInDBException(userId.toString());
        }
        return orderList;
    }

    @Override
    public Optional<Order> create(Order order) {
        entityManager.persist(order);
        return Optional.of(order);
    }

    @Override
    public Long getUserOrderCount(Long userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Order> root = query.from(Order.class);
        query.select(builder.count(root)).distinct(true)
                .where(builder.equal(root.get(ATTRIBUTE_USER_ID), userId));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Long getEntitiesCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(Order.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    private int getStartPosition(int page, int size) {
        return (page - INT_VALUE_OFFSET) * size;
    }
}
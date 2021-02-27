package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderDaoImpl extends CommonOperationDao<Order> implements OrderDao {

    private static final String ATTRIBUTE_USER_ID = "userId";

    public OrderDaoImpl(EntityManager entityManager) {
        super(entityManager, Order.class);
        this.entityManager = entityManager;
    }

    @Override
    public List<Order> readOrdersByUserId(Long userId, int page, int size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root).where(criteriaBuilder.equal(root.get(ATTRIBUTE_USER_ID), userId));
        return entityManager.createQuery(query)
                .setFirstResult(getStartPosition(page, size))
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Optional<Order> create(Order order) {
        associateWithContext(order);
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

    private void associateWithContext(Order order) {
        List<Certificate> detached = order.getCertificates();
        List<Certificate> associated = associateAndGet(detached);
        order.setCertificates(associated);
    }

    private List<Certificate> associateAndGet(List<Certificate> detached) {
        return detached.stream()
                .map(certificate -> entityManager.merge(certificate))
                .collect(Collectors.toList());
    }
}
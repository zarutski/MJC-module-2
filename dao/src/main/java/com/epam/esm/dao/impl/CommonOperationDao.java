package com.epam.esm.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public abstract class CommonOperationDao<T> {

    private static final int PAGE_OFFSET = 1;
    private final Class<T> clazz;

    protected EntityManager entityManager;

    public CommonOperationDao(EntityManager entityManager, Class<T> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    public Optional<T> readById(Long id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    public List<T> readAll(int page, int size) {
        CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(clazz);
        Root<T> root = query.from(clazz);
        query.select(root);
        return entityManager.createQuery(query)
                .setFirstResult(getStartPosition(page, size))
                .setMaxResults(size)
                .getResultList();
    }

    public Long getEntitiesCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(clazz)));
        return entityManager.createQuery(query).getSingleResult();
    }

    public int getStartPosition(int page, int size) {
        return (page - PAGE_OFFSET) * size;
    }

}
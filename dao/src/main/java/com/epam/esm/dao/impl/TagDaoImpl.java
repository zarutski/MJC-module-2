package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.EntityNotInDBException;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Order;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;

import static com.epam.esm.dao.util.ParameterValue.*;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String SELECT_BY_TAG_NAME = "select tag From Tag tag where tag.name=:name";
    private static final String ATTRIBUTE_ORDER_LIST = "orderList";
    private static final String ATTRIBUTE_CERTIFICATE_LIST = "giftCertificateList";
    private static final String COLUMN_ID = "id";

    private final EntityManager entityManager;

    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Tag> readById(final Long tagId) {
        return Optional.ofNullable(entityManager.find(Tag.class, tagId));
    }

    @Override
    public List<Tag> readAll(int page, int size) {
        CriteriaQuery<Tag> query = entityManager.getCriteriaBuilder().createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        int startPosition = (page - INT_VALUE_OFFSET) * size;
        return entityManager.createQuery(query)
                .setFirstResult(startPosition)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Optional<Tag> create(Tag tag) {
        entityManager.persist(tag);
        return Optional.of(tag);
    }

    @Override
    public void deleteById(final Long tagId) {
        Tag tag = entityManager.find(Tag.class, tagId);
        if (entityManager.contains(tag)) {
            entityManager.remove(tag);
        } else {
            throw new EntityNotInDBException(tagId.toString());
        }
    }

    @Override
    public Optional<Tag> readByName(String name) {
        return Optional.of(entityManager.createQuery(SELECT_BY_TAG_NAME, Tag.class)
                .setParameter(PARAMETER_NAME, name)
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new EntityNotInDBException(name)));
    }

    @Override
    public Long getEntitiesCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(Tag.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Optional<Tag> getMostUsedUserTag(Long userID) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> tagQuery = criteriaBuilder.createQuery(Tag.class);
        Root<User> userRoot = tagQuery.from(User.class);

        ListJoin<User, Order> orderList = userRoot.joinList(ATTRIBUTE_ORDER_LIST);
        ListJoin<Order, Certificate> giftList = orderList.joinList(ATTRIBUTE_CERTIFICATE_LIST);
        ListJoin<Certificate, Tag> tagList = giftList.joinList(PARAMETER_TAG);

        Expression orderID = tagList.get(COLUMN_ID);
        tagQuery.select(tagList)
                .where(criteriaBuilder.equal(userRoot.get(COLUMN_ID), userID))
                .groupBy(orderID)
                .orderBy(criteriaBuilder.desc(criteriaBuilder.count(orderID)));

        return Optional.of(entityManager.createQuery(tagQuery).setMaxResults(1).getSingleResult());
    }
}
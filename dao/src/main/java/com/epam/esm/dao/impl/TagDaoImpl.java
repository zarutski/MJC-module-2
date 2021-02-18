package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Order;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import java.util.Optional;

import static com.epam.esm.dao.util.ParameterValue.PARAMETER_NAME;
import static com.epam.esm.dao.util.ParameterValue.PARAMETER_TAG;

@Repository
public class TagDaoImpl extends CommonOperationDao<Tag> implements TagDao {

    private static final String SELECT_BY_TAG_NAME = "select tag From Tag tag where tag.name=:name";
    private static final String ATTRIBUTE_ORDER_LIST = "orderList";
    private static final String ATTRIBUTE_CERTIFICATE_LIST = "certificateList";
    private static final String COLUMN_ID = "id";

    public TagDaoImpl(EntityManager entityManager) {
        super(entityManager, Tag.class);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Tag> create(Tag tag) {
        entityManager.persist(tag);
        return Optional.of(tag);
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }

    @Override
    public Optional<Tag> readByName(String name) {
        return entityManager.createQuery(SELECT_BY_TAG_NAME, Tag.class)
                .setParameter(PARAMETER_NAME, name)
                .getResultStream()
                .findFirst();
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
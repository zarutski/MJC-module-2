package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

import static com.epam.esm.dao.util.ParameterValue.PARAMETER_NAME;

@Repository
public class TagDaoImpl extends CommonOperationDao<Tag> implements TagDao {

    private static final String SELECT_BY_TAG_NAME = "select tag From Tag tag where tag.name=:name";
    private static final String SELECT_MOST_USED_TAG_OF_USER_WITH_HIGHEST_COST = "SELECT t.id, t.name FROM orders AS o " +
            "INNER JOIN gift_certificate_has_orders as gho ON gho.orders_id=o.id " +
            "INNER JOIN gift_certificate_has_tag AS ght ON gho.gift_certificate_id=ght.gift_certificate_id " +
            "INNER JOIN tag AS t ON ght.tag_id=t.id " +
            "WHERE o.user_id = (SELECT user_id FROM orders AS o GROUP by o.user_id order by sum(o.cost) DESC LIMIT 1) " +
            "GROUP BY t.id " +
            "ORDER BY count(*) DESC LIMIT 1";

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
    public Optional<Tag> getMostUsedTagFromUserWithOrdersHighestCost() {
        Query nativeQuery = entityManager.createNativeQuery(SELECT_MOST_USED_TAG_OF_USER_WITH_HIGHEST_COST, Tag.class);
        return Optional.of((Tag) nativeQuery.getSingleResult());
    }
}
package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.exception.EntityNotInDBException;
import com.epam.esm.dao.parameters.SearchQueryProvider;
import com.epam.esm.domain.util.SearchParameter;
import com.epam.esm.domain.entity.Certificate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dao.util.ParameterValue.INT_VALUE_OFFSET;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    private final EntityManager entityManager;
    private final SearchQueryProvider queryProvider;

    public CertificateDaoImpl(EntityManager entityManager, SearchQueryProvider queryProvider) {
        this.entityManager = entityManager;
        this.queryProvider = queryProvider;
    }

    @Override
    public Optional<Certificate> update(Certificate certificate) {
        return Optional.of(entityManager.merge(certificate));
    }

    @Override
    public List<Certificate> searchByParameters(SearchParameter parameter, int page, int limit) {
        CriteriaQuery<Certificate> criteriaQuery = queryProvider.buildQuery(parameter);
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(getStartPosition(page, limit))
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Long getEntitiesCount(SearchParameter searchParameter) {
        CriteriaQuery<Certificate> criteriaQuery = queryProvider.buildQuery(searchParameter);
        return (long) entityManager.createQuery(criteriaQuery).getResultList().size();
    }

    @Override
    public Long getEntitiesCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        query.select(builder.count(query.from(Certificate.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Optional<Certificate> create(Certificate entity) {
        entityManager.persist(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<Certificate> readById(Long id) {
        return Optional.ofNullable(entityManager.find(Certificate.class, id));
    }

    @Override
    public List<Certificate> readAll(int page, int size) {
        CriteriaQuery<Certificate> query = entityManager.getCriteriaBuilder().createQuery(Certificate.class);
        Root<Certificate> root = query.from(Certificate.class);
        query.select(root);
        return entityManager.createQuery(query)
                .setFirstResult(getStartPosition(page, size))
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        if (entityManager.contains(certificate)) {
            entityManager.remove(certificate);
        } else {
            throw new EntityNotInDBException(id.toString());
        }
    }

    private int getStartPosition(int page, int size) {
        return (page - INT_VALUE_OFFSET) * size;
    }
}
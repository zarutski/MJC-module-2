package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.parameters.SearchQueryProvider;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.util.SearchParameter;
import com.epam.esm.domain.entity.Certificate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CertificateDaoImpl extends CommonOperationDao<Certificate> implements CertificateDao {

    private final SearchQueryProvider queryProvider;

    public CertificateDaoImpl(EntityManager entityManager, SearchQueryProvider queryProvider) {
        super(entityManager, Certificate.class);
        this.queryProvider = queryProvider;
    }

    @Override
    public Optional<Certificate> create(Certificate certificate) {
        associateWithContext(certificate);
        entityManager.persist(certificate);
        return Optional.of(certificate);
    }

    @Override
    public void delete(Certificate certificate) {
        entityManager.remove(certificate);
    }

    @Override
    public Optional<Certificate> update(Certificate certificate) {
        entityManager.merge(certificate);
        entityManager.flush();
        return readById(certificate.getId());
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

    private void associateWithContext(Certificate certificate) {
        List<Tag> detached = certificate.getTags();
        List<Tag> associated = associateAndGet(detached);
        certificate.setTags(associated);
    }

    private List<Tag> associateAndGet(List<Tag> detached) {
        return detached.stream()
                .map(tag -> entityManager.merge(tag))
                .collect(Collectors.toList());
    }
}
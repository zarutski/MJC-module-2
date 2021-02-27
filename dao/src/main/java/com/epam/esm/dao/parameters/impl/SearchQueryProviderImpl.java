package com.epam.esm.dao.parameters.impl;

import com.epam.esm.dao.parameters.SearchQueryProvider;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.util.SearchParameter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.epam.esm.dao.util.ParameterValue.PARAMETER_NAME;
import static com.epam.esm.dao.util.ParameterValue.PARAMETER_TAG;

@Component
public class SearchQueryProviderImpl implements SearchQueryProvider {

    private static final String PARAMETER_TAG_NAMES = "tagNames";
    private static final String SELECT_TAG_BY_NAME = "from Tag tag where tag.name in (:" + PARAMETER_TAG_NAMES + ")";
    private static final String PARAMETER_ANY = "%";
    private static final String SORT_TYPE_DEFAULT = "createDate";
    private static final String ORDER_TYPE_DESC = "DESC";
    private static final String ATTRIBUTE_DESCRIPTION = "description";

    private final EntityManager entityManager;

    public SearchQueryProviderImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<Certificate> criteriaQuery;
    private Root<Certificate> certificateRoot;

    public CriteriaQuery<Certificate> buildQuery(SearchParameter parameter) {
        criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        setPredicateList(parameter);
        setColumnSortOrder(parameter.getSortBy(), parameter.getOrder());
        return criteriaQuery;
    }

    private void setPredicateList(SearchParameter parameter) {
        certificateRoot = criteriaQuery.from(Certificate.class);
        List<Predicate> predicateList = buildPredicateList(parameter);
        criteriaQuery.select(certificateRoot).where(predicateList.toArray(new Predicate[0]));
    }

    private List<Predicate> buildPredicateList(SearchParameter parameter) {
        List<Predicate> predicateList = new ArrayList<>();
        String name = parameter.getName();
        if (Objects.nonNull(name)) {
            predicateList.add(buildPredicateAny(PARAMETER_NAME, name));
        }

        String description = parameter.getDescription();
        if (Objects.nonNull(description)) {
            predicateList.add(buildPredicateAny(ATTRIBUTE_DESCRIPTION, description));
        }

        List<String> tagNames = parameter.getTagNames();
        if (tagNames != null && !tagNames.isEmpty()) {
            List<Tag> tags = getTagByNames(tagNames);
            tags.forEach(tag -> predicateList.add(criteriaBuilder.isMember(tag, certificateRoot.get(PARAMETER_TAG))));
        }
        return predicateList;
    }

    private Predicate buildPredicateAny(String attributeName, String attributeValue) {
        return criteriaBuilder.like(certificateRoot.get(attributeName),
                PARAMETER_ANY + attributeValue + PARAMETER_ANY);
    }

    private List<Tag> getTagByNames(List<String> tagNames) {
        List<String> distinctNames = tagNames.stream().distinct().collect(Collectors.toList());
        return entityManager.createQuery(SELECT_TAG_BY_NAME, Tag.class)
                .setParameter(PARAMETER_TAG_NAMES, distinctNames).getResultList();
    }

    private String sortOrDefault(String sortBy) {
        if (PARAMETER_NAME.equalsIgnoreCase(sortBy)) {
            return PARAMETER_NAME;
        }
        return SORT_TYPE_DEFAULT;
    }

    private void setColumnSortOrder(String sortBy, String orderBy) {
        sortBy = sortOrDefault(sortBy);
        Order order;
        if (ORDER_TYPE_DESC.equalsIgnoreCase(orderBy)) {
            order = criteriaBuilder.desc(certificateRoot.get(sortBy));
        } else {
            order = criteriaBuilder.asc(certificateRoot.get(sortBy));
        }
        criteriaQuery.orderBy(order);
    }
}
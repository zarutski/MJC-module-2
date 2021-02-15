package com.epam.esm.dao.parameters.impl;

import com.epam.esm.dao.parameters.SearchQueryProvider;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.util.SearchParameter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
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
        certificateRoot = criteriaQuery.from(Certificate.class);

        // set search predicates
        List<Predicate> predicateList = buildPredicateList(parameter);
        criteriaQuery.select(certificateRoot).where(predicateList.toArray(new Predicate[0]));

        // set sort order
        String sortBy = sortOrDefault(parameter.getSortBy());
        setColumnSortOrder(sortBy, parameter.getOrder());
        return criteriaQuery;
    }

    private List<Predicate> buildPredicateList(SearchParameter parameter) {
        List<Predicate> predicateList = new ArrayList<>();
        String name = parameter.getName();
        if (Objects.nonNull(name)) {
            Predicate predicate = criteriaBuilder.like(certificateRoot.get(PARAMETER_NAME),
                    PARAMETER_ANY + name + PARAMETER_ANY);
            predicateList.add(predicate);
        }

        String description = parameter.getDescription();
        if (Objects.nonNull(description)) {
            Predicate predicate = criteriaBuilder.like(certificateRoot.get(ATTRIBUTE_DESCRIPTION),
                    PARAMETER_ANY + description + PARAMETER_ANY);
            predicateList.add(predicate);
        }

        List<String> tagNames = parameter.getTagNames();
        if (tagNames != null && !tagNames.isEmpty()) {
            List<String> distinctNames = tagNames.stream().distinct().collect(Collectors.toList());
            List<Tag> tags = entityManager.createQuery(SELECT_TAG_BY_NAME, Tag.class)
                    .setParameter(PARAMETER_TAG_NAMES, distinctNames).getResultList();
            tags.forEach(tag -> predicateList.add(criteriaBuilder.isMember(tag, certificateRoot.get(PARAMETER_TAG))));
        }
        return predicateList;
    }


    private String sortOrDefault(String sortBy) {
        if (PARAMETER_NAME.equalsIgnoreCase(sortBy)) {
            return PARAMETER_NAME;
        }
        return SORT_TYPE_DEFAULT;
    }

    private void setColumnSortOrder(String sortBy, String orderBy) {
        Order order;
        if (ORDER_TYPE_DESC.equalsIgnoreCase(orderBy)) {
            order = criteriaBuilder.desc(certificateRoot.get(sortBy));
        } else {
            order = criteriaBuilder.asc(certificateRoot.get(sortBy));
        }
        criteriaQuery.orderBy(order);
    }
}
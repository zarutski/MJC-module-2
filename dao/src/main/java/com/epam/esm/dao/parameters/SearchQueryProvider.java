package com.epam.esm.dao.parameters;

import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.util.SearchParameter;

import javax.persistence.criteria.CriteriaQuery;

public interface SearchQueryProvider {

    CriteriaQuery<Certificate> buildQuery(SearchParameter parameter);
}
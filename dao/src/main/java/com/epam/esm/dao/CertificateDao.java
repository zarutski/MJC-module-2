package com.epam.esm.dao;

import com.epam.esm.domain.util.SearchParameter;
import com.epam.esm.domain.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateDao extends DBOperationCD<Certificate>, CommonOperation<Certificate> {

    Optional<Certificate> update(Certificate certificate);

    List<Certificate> searchByParameters(SearchParameter parameter, int page, int limit);

    Long getEntitiesCount(SearchParameter searchParameter);

}
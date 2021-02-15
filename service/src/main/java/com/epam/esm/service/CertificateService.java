package com.epam.esm.service;

import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.util.SearchParameter;

import java.util.List;

public interface CertificateService extends CRDService<CertificateDTO> {

    CertificateDTO update(CertificateDTO certificate);

    List<CertificateDTO> searchByParameters(SearchParameter searchParameter, int page, int size);

    Long getEntitiesCount(SearchParameter searchParameter);

}
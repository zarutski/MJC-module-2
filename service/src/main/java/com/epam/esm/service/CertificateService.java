package com.epam.esm.service;

import com.epam.esm.domain.dto.CertificateDTO;

import java.util.List;

public interface CertificateService extends CRDService<CertificateDTO> {

    Integer updateCertificate(CertificateDTO certificate);

    List<CertificateDTO> searchByParameters(String tagName, String certificateName, String description, String sortBy, String order);
}
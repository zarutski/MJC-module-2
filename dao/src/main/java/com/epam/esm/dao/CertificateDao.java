package com.epam.esm.dao;

import com.epam.esm.domain.entity.Certificate;

import java.util.List;

public interface CertificateDao extends DBOperationCRD<Certificate> {

    Integer updateCertificate(Certificate certificate);

    Integer createCertificateHasTag(Long certificateId, Long tagId);

    List<Certificate> searchByParameters(String query, List<String> parameters);

}
package com.epam.esm.service.mapper;

import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Certificate;

import java.util.List;

public interface CertificateDTOMapper {

    Certificate toCertificateEntity(CertificateDTO dto);

    CertificateDTO toCertificateDto(Certificate certificate, List<TagDTO> tagList);
}
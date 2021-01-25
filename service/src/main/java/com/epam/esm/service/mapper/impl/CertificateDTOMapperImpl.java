package com.epam.esm.service.mapper.impl;

import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.service.mapper.CertificateDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CertificateDTOMapperImpl implements CertificateDTOMapper {

    @Override
    public Certificate toCertificateEntity(CertificateDTO dto) {
        Certificate certificate = new Certificate();
        certificate.setId(dto.getId());
        certificate.setName(dto.getName());
        certificate.setDescription(dto.getDescription());
        certificate.setPrice(dto.getPrice());
        certificate.setDuration(dto.getDuration());
        certificate.setCreateDate(dto.getCreateDate());
        certificate.setLastUpdateDate(dto.getLastUpdateDate());
        return certificate;
    }

    @Override
    public CertificateDTO toCertificateDto(Certificate certificate, List<TagDTO> tagList) {
        CertificateDTO dto = new CertificateDTO();
        dto.setId(certificate.getId());
        dto.setName(certificate.getName());
        dto.setDescription(certificate.getDescription());
        dto.setPrice(certificate.getPrice());
        dto.setDuration(certificate.getDuration());
        dto.setCreateDate(certificate.getCreateDate());
        dto.setLastUpdateDate(certificate.getLastUpdateDate());
        dto.setTags(tagList);
        return dto;
    }
}

package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.parameters.ParametersProvider;
import com.epam.esm.dao.parameters.SearchQueryProvider;
import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IdNotExistException;
import com.epam.esm.service.mapper.CertificateDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDAO;
    private final CertificateDTOMapper certificateDTOMapper;
    private final TagService tagService;
    private final SearchQueryProvider queryProvider;
    private final ParametersProvider queryParametersProvider;

    public CertificateServiceImpl(CertificateDao certificateDAO, CertificateDTOMapper certificateDTOMapper,
                                  TagService tagService, SearchQueryProvider queryProvider,
                                  ParametersProvider queryParametersProvider) {
        this.certificateDAO = certificateDAO;
        this.certificateDTOMapper = certificateDTOMapper;
        this.tagService = tagService;
        this.queryProvider = queryProvider;
        this.queryParametersProvider = queryParametersProvider;
    }

    @Override
    @Transactional
    public Integer update(CertificateDTO certificate) {
        return certificateDAO.update(certificateDTOMapper.toEntity(certificate));
    }

    @Override
    public List<CertificateDTO> searchByParameters(String tagName, String certificateName, String description, String sortBy, String order) {
        String queryPart = queryProvider.getCertificateSearchQuery(tagName, certificateName, description, sortBy, order);
        List<String> queryParameters = queryParametersProvider.getCertificateParameters(tagName, certificateName, description);

        return certificateDAO.searchByParameters(queryPart, queryParameters)
                .stream()
                .map(certificate -> certificateDTOMapper.toDto(certificate, tagService.readByCertificateId(certificate.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long create(CertificateDTO dto) {
        // insert certificate
        Certificate certificateEntity = certificateDTOMapper.toEntity(dto);
        Long certificateId = certificateDAO.create(certificateEntity);

        // insert tags
        List<TagDTO> tagsDTO = dto.getTags();
        tagsDTO.forEach(tagDTO -> {
            Long tagId = tagService.readByName(tagDTO.getName()).map(Tag::getId).orElseGet(() -> tagService.create(tagDTO));
            certificateDAO.createCertificateHasTag(certificateId, tagId);
        });
        return certificateId;
    }

    @Override
    public CertificateDTO readById(Long id) {
        return certificateDAO.readById(id)
                .map(certificate -> certificateDTOMapper.toDto(certificate, tagService.readByCertificateId(id)))
                .orElseThrow(() -> new IdNotExistException(id.toString()));
    }

    @Override
    public List<CertificateDTO> readAll() {
        return certificateDAO.readAll()
                .stream()
                .map(certificate -> certificateDTOMapper.toDto(certificate, tagService.readByCertificateId(certificate.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Integer deleteById(Long id) {
        return certificateDAO.deleteById(id);
    }

}
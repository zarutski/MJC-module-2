package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.service.exception.UpdateEntityInternalException;
import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.util.SearchParameter;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.exception.IdNotExistException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDAO;
    private final ModelMapper modelMapper;
    private final TagService tagService;

    public CertificateServiceImpl(CertificateDao certificateDAO, ModelMapper modelMapper, TagService tagService) {
        this.certificateDAO = certificateDAO;
        this.modelMapper = modelMapper;
        this.tagService = tagService;
    }

    @Override
    public CertificateDTO update(CertificateDTO certificateDTO) {
        Certificate existing = modelMapper.map(readById(certificateDTO.getId()), Certificate.class);
        updateFieldsFromDto(existing, certificateDTO);
        existing = certificateDAO.update(existing).orElseThrow(UpdateEntityInternalException::new);
        return modelMapper.map(existing, CertificateDTO.class);
    }

    @Override
    public List<CertificateDTO> searchByParameters(SearchParameter searchParameter, int page, int size) {
        List<Certificate> certificates = certificateDAO.searchByParameters(searchParameter, page, size);
        return certificates.stream()
                .map(certificate -> modelMapper.map(certificate, CertificateDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CertificateDTO create(CertificateDTO certificateDTO) {
        Certificate certificate = modelMapper.map(certificateDTO, Certificate.class);
        List<Tag> tags = prepareCertificateTags(certificateDTO.getTags());
        certificate.setTags(tags);
        Certificate created = certificateDAO.create(certificate)
                .orElseThrow(CreateEntityInternalException::new);
        return modelMapper.map(created, CertificateDTO.class);
    }

    @Override
    public CertificateDTO readById(Long id) {
        return certificateDAO.readById(id)
                .map(certificate -> modelMapper.map(certificate, CertificateDTO.class))
                .orElseThrow(() -> new IdNotExistException(id.toString()));
    }

    @Override
    public List<CertificateDTO> readAll(int page, int size) {
        return certificateDAO.readAll(page, size)
                .stream()
                .map(certificate -> modelMapper.map(certificate, CertificateDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Certificate certificate = certificateDAO.readById(id).orElseThrow(() -> new IdNotExistException(id.toString()));
        certificateDAO.delete(certificate);
    }

    @Override
    public Long getEntitiesCount(SearchParameter searchParameter) {
        return certificateDAO.getEntitiesCount(searchParameter);
    }

    @Override
    public Long getEntitiesCount() {
        return certificateDAO.getEntitiesCount();
    }

    private List<Tag> prepareCertificateTags(List<TagDTO> tagDTOList) {
        return tagDTOList.stream()
                .map(tagDTO -> modelMapper.map(getTagOrCreate(tagDTO), Tag.class))
                .collect(Collectors.toList());
    }

    private TagDTO getTagOrCreate(TagDTO tagDTO) {
        try {
            return tagService.readByName(tagDTO.getName());
        } catch (IdNotExistException e) {
            return tagService.create(tagDTO);
        }
    }

    private void updateFieldsFromDto(Certificate existingCertificate, CertificateDTO certificateDTO) {
        if (Objects.nonNull(certificateDTO.getName())) {
            existingCertificate.setName(certificateDTO.getName());
        }
        if (Objects.nonNull(certificateDTO.getDescription())) {
            existingCertificate.setDescription(certificateDTO.getDescription());
        }
        if (Objects.nonNull(certificateDTO.getPrice())) {
            existingCertificate.setPrice(certificateDTO.getPrice());
        }
        if (Objects.nonNull((certificateDTO.getDuration()))) {
            existingCertificate.setDuration(certificateDTO.getDuration());
        }
    }
}
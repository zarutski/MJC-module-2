package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
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
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDAO;
    private final TagDao tagDao;
    private final ModelMapper modelMapper;

    public CertificateServiceImpl(CertificateDao certificateDAO, TagDao tagDao, ModelMapper modelMapper) {
        this.certificateDAO = certificateDAO;
        this.tagDao = tagDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CertificateDTO update(CertificateDTO dto) {
        Certificate existing = certificateDAO.readById(dto.getId())
                .orElseThrow(() -> new IdNotExistException(dto.getId().toString()));
        updateFieldsFromDto(existing, dto);
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
    @Transactional
    public CertificateDTO create(CertificateDTO dto) {
        Certificate certificate = modelMapper.map(dto, Certificate.class);
        // get tags or create new ones
        List<Tag> tags = dto.getTags().stream()
                .map(tagDTO ->
                        tagDao.readByName(tagDTO.getName())
                                .orElseGet(() -> tagDao.create(modelMapper.map(tagDTO, Tag.class))
                                        .orElseThrow(CreateEntityInternalException::new))
                )
                .collect(Collectors.toList());
        certificate.setTags(tags);

        // create certificate
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
    @Transactional
    public void deleteById(Long id) {
        certificateDAO.deleteById(id);
    }

    @Override
    public Long getEntitiesCount(SearchParameter searchParameter) {
        return certificateDAO.getEntitiesCount(searchParameter);
    }

    @Override
    public Long getEntitiesCount() {
        return certificateDAO.getEntitiesCount();
    }


    private void updateFieldsFromDto(Certificate existing, CertificateDTO dto) {
        if (Objects.nonNull(dto.getName())) {
            existing.setName(dto.getName());
        }
        if (Objects.nonNull(dto.getDescription())) {
            existing.setDescription(dto.getDescription());
        }
        if (Objects.nonNull(dto.getPrice())) {
            existing.setPrice(dto.getPrice());
        }
        if (Objects.nonNull((dto.getDuration()))) {
            existing.setDuration(dto.getDuration());
        }
    }
}
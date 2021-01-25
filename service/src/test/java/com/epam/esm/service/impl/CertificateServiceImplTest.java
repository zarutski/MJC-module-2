package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.parameters.ParametersProvider;
import com.epam.esm.dao.parameters.SearchQueryProvider;
import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IdNotExistException;
import com.epam.esm.service.mapper.CertificateDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceImplTest {

    @Mock
    private CertificateService certificateService;
    @Mock
    private CertificateDao certificateDao;
    @Mock
    private CertificateDTOMapper certificateDTOMapper;
    @Mock
    private TagService tagService;
    @Mock
    private SearchQueryProvider queryProvider;
    @Mock
    private ParametersProvider queryParametersProvider;

    private static final String QUERY_PART = "query stub";
    private static final String PARAMETER_STUB = "parameter stub";
    private static final Integer RECORDS_AFFECTED_ONE = 1;
    private static final Integer RECORDS_AFFECTED_NONE = 0;
    private static final Long RECORD_ID = 1L;

    @BeforeEach
    public void setup() {
        certificateService =
                new CertificateServiceImpl(certificateDao, certificateDTOMapper, tagService, queryProvider, queryParametersProvider);
    }

    @Test
    void updateCertificatePositive() {
        Certificate certificate = new Certificate();
        CertificateDTO certificateDTO = new CertificateDTO();

        when(certificateDao.updateCertificate(certificate)).thenReturn(RECORDS_AFFECTED_ONE);
        when(certificateDTOMapper.toCertificateEntity(certificateDTO)).thenReturn(certificate);

        assertEquals(RECORDS_AFFECTED_ONE, certificateService.updateCertificate(certificateDTO));
    }

    @Test
    void updateCertificateNegative() {
        Certificate certificate = new Certificate();
        CertificateDTO certificateDTO = new CertificateDTO();

        when(certificateDao.updateCertificate(certificate)).thenReturn(RECORDS_AFFECTED_NONE);
        when(certificateDTOMapper.toCertificateEntity(certificateDTO)).thenReturn(certificate);

        assertNotEquals(RECORDS_AFFECTED_ONE, certificateService.updateCertificate(certificateDTO));
    }

    @Test
    void searchByParametersPositive() {
        List<String> queryParameters = new ArrayList<>();

        Certificate certificate = new Certificate();
        List<Certificate> certificates = Stream.of(certificate).collect(Collectors.toList());
        List<TagDTO> tagsDTO = new ArrayList<>();
        CertificateDTO certificateDTO = new CertificateDTO();
        List<CertificateDTO> expected = Stream.of(certificateDTO).collect(Collectors.toList());

        when(queryProvider.getCertificateSearchQuery(PARAMETER_STUB, PARAMETER_STUB, PARAMETER_STUB, PARAMETER_STUB, PARAMETER_STUB)).thenReturn(QUERY_PART);
        when(queryParametersProvider.getCertificateParameters(PARAMETER_STUB, PARAMETER_STUB, PARAMETER_STUB)).thenReturn(queryParameters);
        when(certificateDao.searchByParameters(QUERY_PART, queryParameters)).thenReturn(certificates);
        when(certificateDTOMapper.toCertificateDto(certificate, tagsDTO)).thenReturn(certificateDTO);

        List<CertificateDTO> actual = certificateService.searchByParameters(PARAMETER_STUB, PARAMETER_STUB, PARAMETER_STUB, PARAMETER_STUB, PARAMETER_STUB);
        assertEquals(expected, actual);
    }

    @Test
    void readAllPositive() {
        Certificate certificate = new Certificate();
        List<Certificate> certificates = Stream.of(certificate).collect(Collectors.toList());

        List<TagDTO> tagsDTO = new ArrayList<>();
        CertificateDTO certificateDTO = new CertificateDTO();
        List<CertificateDTO> expected = Stream.of(certificateDTO).collect(Collectors.toList());

        when(certificateDao.readAll()).thenReturn(certificates);
        when(certificateDTOMapper.toCertificateDto(certificate, tagsDTO)).thenReturn(certificateDTO);

        assertEquals(expected, certificateService.readAll());
    }

    @Test
    void readByIdPositive() {
        Certificate certificate = new Certificate();
        List<TagDTO> tagDTOList = new ArrayList<>();
        CertificateDTO expected = new CertificateDTO();

        when(certificateDao.readById(RECORD_ID)).thenReturn(Optional.of(certificate));
        when(certificateDTOMapper.toCertificateDto(certificate, tagDTOList)).thenReturn(expected);
        when(tagService.readTagsByCertificateId(RECORD_ID)).thenReturn(tagDTOList);

        assertEquals(expected, certificateService.readById(RECORD_ID));
    }

    @Test
    void readByIdNegative() {
        when(certificateDao.readById(RECORD_ID)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> certificateService.readById(RECORD_ID));
    }

    @Test
    void createPositive() {
        Certificate certificate = new Certificate();
        CertificateDTO certificateDTO = new CertificateDTO();
        TagDTO tagDTO = new TagDTO();
        List<TagDTO> tagDTOList = Stream.of(tagDTO).collect(Collectors.toList());
        certificateDTO.setTags(tagDTOList);

        when(certificateDao.createFromEntity(certificate)).thenReturn(RECORD_ID);
        when(certificateDTOMapper.toCertificateEntity(certificateDTO)).thenReturn(certificate);
        when(tagService.create(tagDTO)).thenReturn(RECORD_ID);

        assertEquals(RECORD_ID, certificateService.create(certificateDTO));
    }

    @Test
    void createNegative() {
        Certificate certificate = new Certificate();
        CertificateDTO certificateDTO = new CertificateDTO();

        // error during sql query
        when(certificateDao.createFromEntity(certificate)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> certificateService.create(certificateDTO));
    }

    @Test
    void deleteByIdPositive() {
        when(certificateDao.deleteById(RECORD_ID)).thenReturn(RECORDS_AFFECTED_ONE);
        assertEquals(RECORDS_AFFECTED_ONE, certificateService.deleteById(RECORD_ID));
    }

    @Test
    void deleteByIdNegative() {
        when(certificateDao.deleteById(RECORD_ID)).thenReturn(RECORDS_AFFECTED_NONE);
        assertNotEquals(RECORDS_AFFECTED_ONE, certificateService.deleteById(RECORD_ID));
    }

}
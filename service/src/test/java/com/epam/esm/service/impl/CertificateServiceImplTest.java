package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.domain.dto.CertificateDTO;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.domain.util.SearchParameter;
import com.epam.esm.service.exception.IdNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceImplTest {

    @Mock
    private CertificateDao certificateDao;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TagService tagService;
    @InjectMocks
    private CertificateServiceImpl certificateService;

    private static final String TAG_NAME = "tag name";
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer RECORDS_PER_PAGE = 3;
    private static final Long RECORD_ID = 1L;

    @Test
    void updateCertificatePositive() {
        Certificate certificate = new Certificate();
        CertificateDTO certificateDTO = new CertificateDTO();

        when(certificateDao.readById(certificateDTO.getId())).thenReturn(Optional.of(certificate));
        when(certificateDao.update(certificate)).thenReturn(Optional.of(certificate));
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);
        when(modelMapper.map(certificateDTO, Certificate.class)).thenReturn(certificate);
        assertEquals(certificateDTO, certificateService.update(certificateDTO));
    }

    @Test
    void updateCertificateNegative() {
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setId(RECORD_ID);

        when(certificateDao.readById(certificateDTO.getId())).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> certificateService.update(certificateDTO));
    }

    @Test
    void searchByParametersPositive() {
        Certificate certificate = new Certificate();
        List<Certificate> certificates = Stream.of(certificate).collect(Collectors.toList());
        CertificateDTO certificateDTO = new CertificateDTO();
        List<CertificateDTO> expected = Stream.of(certificateDTO).collect(Collectors.toList());
        SearchParameter searchParameter = new SearchParameter();

        when(certificateDao.searchByParameters(searchParameter, PAGE_NUMBER, RECORDS_PER_PAGE)).thenReturn(certificates);
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);

        List<CertificateDTO> actual = certificateService.searchByParameters(searchParameter, PAGE_NUMBER, RECORDS_PER_PAGE);
        assertEquals(expected, actual);
    }

    @Test
    void readAllPositive() {
        Certificate certificate = new Certificate();
        List<Certificate> certificates = Stream.of(certificate).collect(Collectors.toList());
        CertificateDTO certificateDTO = new CertificateDTO();
        List<CertificateDTO> expected = Stream.of(certificateDTO).collect(Collectors.toList());

        when(certificateDao.readAll(PAGE_NUMBER, RECORDS_PER_PAGE)).thenReturn(certificates);
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);
        assertEquals(expected, certificateService.readAll(PAGE_NUMBER, RECORDS_PER_PAGE));
    }

    @Test
    void readByIdPositive() {
        Certificate certificate = new Certificate();
        CertificateDTO expected = new CertificateDTO();

        when(certificateDao.readById(RECORD_ID)).thenReturn(Optional.of(certificate));
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(expected);
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
        Tag tag = new Tag();
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(TAG_NAME);
        List<TagDTO> tagDTOList = Stream.of(tagDTO).collect(Collectors.toList());
        certificateDTO.setTags(tagDTOList);

        when(modelMapper.map(certificateDTO, Certificate.class)).thenReturn(certificate);
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);
        when(modelMapper.map(tagDTO, Tag.class)).thenReturn(tag);
        when(certificateDao.create(certificate)).thenReturn(Optional.of(certificate));
        when(tagService.readByName(TAG_NAME)).thenReturn(tagDTO);

        assertEquals(certificateDTO, certificateService.create(certificateDTO));
    }

    @Test
    void createNegative() {
        CertificateDTO certificateDTO = new CertificateDTO();
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(TAG_NAME);
        List<TagDTO> tagDTOList = Stream.of(tagDTO).collect(Collectors.toList());
        certificateDTO.setTags(tagDTOList);

        when(modelMapper.map(certificateDTO, Certificate.class)).thenReturn(new Certificate());
        when(tagService.readByName(TAG_NAME)).thenThrow(new IdNotExistException());
        when(tagService.create(tagDTO)).thenThrow(new CreateEntityInternalException());
        assertThrows(CreateEntityInternalException.class, () -> certificateService.create(certificateDTO));
    }

    @Test
    void deleteByIdPositive() {
        when(certificateDao.readById(RECORD_ID)).thenReturn(Optional.of(new Certificate()));
        assertDoesNotThrow(() -> certificateService.deleteById(RECORD_ID));
    }

    @Test
    void deleteByIdNegative() {
        when(certificateDao.readById(RECORD_ID)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> certificateService.deleteById(RECORD_ID));
    }
}
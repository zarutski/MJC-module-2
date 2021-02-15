package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.dao.exception.EntityNotInDBException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CertificateServiceImplTest {

    @Mock
    private CertificateDao certificateDao;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TagDao tagDao;
    @InjectMocks
    private CertificateServiceImpl certificateService;

    private static final String TAG_NAME = "tag name";
    private static final Integer VALUE_PAGE = 1;
    private static final Integer VALUE_SIZE = 3;
    private static final Long LONG_VALUE_ONE = 1L;

    @Test
    void updateCertificatePositive() {
        Certificate certificate = new Certificate();
        CertificateDTO certificateDTO = new CertificateDTO();

        when(certificateDao.readById(certificateDTO.getId())).thenReturn(Optional.of(certificate));
        when(certificateDao.update(certificate)).thenReturn(Optional.of(certificate));
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);
        assertEquals(certificateDTO, certificateService.update(certificateDTO));
    }

    @Test
    void updateCertificateNegative() {
        CertificateDTO certificateDTO = new CertificateDTO();
        certificateDTO.setId(LONG_VALUE_ONE);

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

        when(certificateDao.searchByParameters(searchParameter, VALUE_PAGE, VALUE_SIZE)).thenReturn(certificates);
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);

        List<CertificateDTO> actual = certificateService.searchByParameters(searchParameter, VALUE_PAGE, VALUE_SIZE);
        assertEquals(expected, actual);
    }

    @Test
    void readAllPositive() {
        Certificate certificate = new Certificate();
        List<Certificate> certificates = Stream.of(certificate).collect(Collectors.toList());
        CertificateDTO certificateDTO = new CertificateDTO();
        List<CertificateDTO> expected = Stream.of(certificateDTO).collect(Collectors.toList());

        when(certificateDao.readAll(VALUE_PAGE, VALUE_SIZE)).thenReturn(certificates);
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);
        assertEquals(expected, certificateService.readAll(VALUE_PAGE, VALUE_SIZE));
    }

    @Test
    void readByIdPositive() {
        Certificate certificate = new Certificate();
        CertificateDTO expected = new CertificateDTO();

        when(certificateDao.readById(LONG_VALUE_ONE)).thenReturn(Optional.of(certificate));
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(expected);
        assertEquals(expected, certificateService.readById(LONG_VALUE_ONE));
    }

    @Test
    void readByIdNegative() {
        when(certificateDao.readById(LONG_VALUE_ONE)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> certificateService.readById(LONG_VALUE_ONE));
    }

    @Test
    void createPositive() {
        Certificate certificate = new Certificate();
        CertificateDTO certificateDTO = new CertificateDTO();
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(TAG_NAME);
        List<TagDTO> tagDTOList = Stream.of(tagDTO).collect(Collectors.toList());
        certificateDTO.setTags(tagDTOList);

        when(modelMapper.map(certificateDTO, Certificate.class)).thenReturn(certificate);
        when(tagDao.readByName(TAG_NAME)).thenReturn(Optional.of(new Tag()));
        when(certificateDao.create(certificate)).thenReturn(Optional.of(certificate));
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);

        assertEquals(certificateDTO, certificateService.create(certificateDTO));
    }

    @Test
    void createNegative() {
        Tag tag = new Tag();
        CertificateDTO certificateDTO = new CertificateDTO();
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName(TAG_NAME);
        List<TagDTO> tagDTOList = Stream.of(tagDTO).collect(Collectors.toList());
        certificateDTO.setTags(tagDTOList);

        // error creating entity
        when(modelMapper.map(certificateDTO, Certificate.class)).thenReturn(new Certificate());
        when(tagDao.readByName(TAG_NAME)).thenReturn(Optional.empty());
        when(tagDao.create(tag)).thenReturn(Optional.empty());
        when(modelMapper.map(tagDTO, Tag.class)).thenReturn(tag);

        assertThrows(CreateEntityInternalException.class, () -> certificateService.create(certificateDTO));
    }

    @Test
    void deleteByIdPositive() {
        assertDoesNotThrow(() -> certificateService.deleteById(LONG_VALUE_ONE));
    }

    @Test
    void deleteByIdNegative() {
        doThrow(new EntityNotInDBException()).when(certificateDao).deleteById(LONG_VALUE_ONE);
        assertThrows(EntityNotInDBException.class, () -> certificateService.deleteById(LONG_VALUE_ONE));
    }
}
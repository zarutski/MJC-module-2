package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.exception.IdNotExistException;
import com.epam.esm.service.mapper.TagDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagDao tagDao;
    @Mock
    private TagDTOMapper tagDTOMapper;
    @InjectMocks
    private TagServiceImpl tagService;

    private static final Long RECORD_ID = 1L;
    private static final Integer RECORDS_AFFECTED_ONE = 1;
    private static final Integer RECORDS_AFFECTED_NONE = 0;

    @Test
    void readTagsByCertificateIdPositive() {
        List<Tag> tags = new ArrayList<>();
        List<TagDTO> expected = new ArrayList<>();

        when(tagDao.readByCertificateId(RECORD_ID)).thenReturn(tags);
        when(tagDTOMapper.toDTOList(tags)).thenReturn(expected);

        assertEquals(expected, tagService.readByCertificateId(RECORD_ID));
    }

    @Test
    void readTagsByCertificateIdNegative() {
        List<Tag> tags = new ArrayList<>();
        List<TagDTO> expected = new ArrayList<>();
        TagDTO tagDTO = new TagDTO();
        expected.add(tagDTO);

        when(tagDao.readByCertificateId(RECORD_ID)).thenReturn(tags);
        when(tagDTOMapper.toDTOList(tags)).thenReturn(new ArrayList<>());

        assertNotEquals(expected, tagService.readByCertificateId(RECORD_ID));
    }

    @Test
    void createAlreadyInDB() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();
        tag.setId(RECORD_ID);

        when(tagDao.create(tag)).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> tagService.create(tagDTO));
    }

    @Test
    void createPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(tagDao.create(tag)).thenReturn(RECORD_ID);
        when(tagDTOMapper.toEntity(tagDTO)).thenReturn(tag);

        assertEquals(RECORD_ID, tagService.create(tagDTO));
    }

    @Test
    void createNegative() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(tagDao.readByName(tagDTO.getName())).thenReturn(Optional.empty());
        when(tagDao.create(tag)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> tagService.create(tagDTO));
    }

    @Test
    void readByIdPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();
        when(tagDTOMapper.toDTO(tag)).thenReturn(tagDTO);
        when(tagDao.readById(RECORD_ID)).thenReturn(Optional.of(tag));

        assertEquals(tagDTO, tagService.readById(RECORD_ID));
    }

    @Test
    void readByIdNegative() {
        when(tagDao.readById(RECORD_ID)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> tagService.readById(RECORD_ID));
    }

    @Test
    void readAllPositive() {
        List<Tag> tags = new ArrayList<>();
        List<TagDTO> expected = new ArrayList<>();

        when(tagDao.readAll()).thenReturn(tags);
        assertEquals(expected, tagService.readAll());
    }

    @Test
    void deleteByIdPositive() {
        when(tagDao.deleteById(RECORD_ID)).thenReturn(RECORDS_AFFECTED_ONE);
        assertEquals(RECORDS_AFFECTED_ONE, tagService.deleteById(RECORD_ID));
    }

    @Test
    void deleteByIdNegative() {
        when(tagDao.deleteById(RECORD_ID)).thenReturn(RECORDS_AFFECTED_NONE);
        assertNotEquals(RECORDS_AFFECTED_ONE, tagService.deleteById(RECORD_ID));
    }

}
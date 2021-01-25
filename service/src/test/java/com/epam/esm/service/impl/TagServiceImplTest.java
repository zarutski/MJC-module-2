package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IdNotExistException;
import com.epam.esm.service.mapper.TagDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private TagService tagService;
    @Mock
    private TagDao tagDao;
    @Mock
    private TagDTOMapper tagDTOMapper;

    private static final Long RECORD_ID = 1L;
    private static final Integer RECORDS_AFFECTED_ONE = 1;
    private static final Integer RECORDS_AFFECTED_NONE = 0;

    @BeforeEach
    public void setup() {
        tagService = new TagServiceImpl(tagDao, tagDTOMapper);
    }

    @Test
    void readTagsByCertificateIdPositive() {
        List<Tag> tags = new ArrayList<>();
        List<TagDTO> expected = new ArrayList<>();

        when(tagDao.readTagsByCertificateId(RECORD_ID)).thenReturn(tags);
        when(tagDTOMapper.toTagDTOList(tags)).thenReturn(expected);

        assertEquals(expected, tagService.readTagsByCertificateId(RECORD_ID));
    }

    @Test
    void readTagsByCertificateIdNegative() {
        List<Tag> tags = new ArrayList<>();
        List<TagDTO> expected = new ArrayList<>();
        TagDTO tagDTO = new TagDTO();
        expected.add(tagDTO);

        when(tagDao.readTagsByCertificateId(RECORD_ID)).thenReturn(tags);
        when(tagDTOMapper.toTagDTOList(tags)).thenReturn(new ArrayList<>());

        assertNotEquals(expected, tagService.readTagsByCertificateId(RECORD_ID));
    }

    @Test
    void createAlreadyInDBPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();
        tag.setId(RECORD_ID);

        when(tagDao.readByTagName(tagDTO.getName())).thenReturn(Optional.of(tag));
        Long expected = tag.getId();
        Long actual = tagService.create(tagDTO);

        assertEquals(expected, actual);
    }

    @Test
    void createPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(tagDao.readByTagName(tagDTO.getName())).thenReturn(Optional.empty());
        when(tagDao.createFromEntity(tag)).thenReturn(RECORD_ID);
        when(tagDTOMapper.toTagEntity(tagDTO)).thenReturn(tag);

        assertEquals(RECORD_ID, tagService.create(tagDTO));
    }

    @Test
    void createNegative() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(tagDao.readByTagName(tagDTO.getName())).thenReturn(Optional.empty());
        when(tagDao.createFromEntity(tag)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> tagService.create(tagDTO));
    }

    @Test
    void readByIdPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();
        when(tagDTOMapper.toTagDTO(tag)).thenReturn(tagDTO);
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
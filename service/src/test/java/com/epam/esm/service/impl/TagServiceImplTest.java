package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.exception.IdNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagDao tagDao;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private TagServiceImpl tagService;

    private static final Integer PAGE_NUMBER = 1;
    private static final Integer RECORDS_PER_PAGE = 3;
    private static final Long RECORD_ID = 1L;

    @Test
    void createAlreadyInDB() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(tagDao.create(tag)).thenThrow(new RuntimeException());
        when(modelMapper.map(tagDTO, Tag.class)).thenReturn(tag);
        assertThrows(RuntimeException.class, () -> tagService.create(tagDTO));
    }

    @Test
    void createPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(tagDao.create(tag)).thenReturn(Optional.of(tag));
        when(modelMapper.map(tagDTO, Tag.class)).thenReturn(tag);
        when(modelMapper.map(tag, TagDTO.class)).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.create(tagDTO));
    }

    @Test
    void createNegative() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(tagDao.create(tag)).thenReturn(Optional.empty());
        when(modelMapper.map(tagDTO, Tag.class)).thenReturn(tag);
        assertThrows(CreateEntityInternalException.class, () -> tagService.create(tagDTO));
    }

    @Test
    void readByIdPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(modelMapper.map(tag, TagDTO.class)).thenReturn(tagDTO);
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

        when(tagDao.readAll(PAGE_NUMBER, RECORDS_PER_PAGE)).thenReturn(tags);
        assertEquals(expected, tagService.readAll(PAGE_NUMBER, RECORDS_PER_PAGE));
    }

    @Test
    void deleteByIdPositive() {
        when(tagDao.readById(RECORD_ID)).thenReturn(Optional.of(new Tag()));
        assertDoesNotThrow(() -> tagService.deleteById(RECORD_ID));
    }

    @Test
    void deleteByIdNegative() {
        when(tagDao.readById(RECORD_ID)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> tagService.deleteById(RECORD_ID));
    }

    @Test
    void getMostUsedTagPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(userService.getUserIdWithOrdersHighestCost()).thenReturn(RECORD_ID);
        when(tagDao.getMostUsedUserTag(RECORD_ID)).thenReturn(Optional.of(tag));
        when(modelMapper.map(tag, TagDTO.class)).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.getMostUsedTagFromUserWithOrdersHighestCost());
    }

    @Test
    void getMostUsedTagNegative() {
        when(userService.getUserIdWithOrdersHighestCost()).thenReturn(RECORD_ID);
        when(tagDao.getMostUsedUserTag(RECORD_ID)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> tagService.getMostUsedTagFromUserWithOrdersHighestCost());
    }

}
package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.dao.exception.EntityNotInDBException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagDao tagDao;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private TagServiceImpl tagService;

    private static final Integer VALUE_PAGE = 1;
    private static final Integer VALUE_SIZE = 3;
    private static final Long LONG_VALUE_ONE = 1L;

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
        when(tagDao.readById(LONG_VALUE_ONE)).thenReturn(Optional.of(tag));
        assertEquals(tagDTO, tagService.readById(LONG_VALUE_ONE));
    }

    @Test
    void readByIdNegative() {
        when(tagDao.readById(LONG_VALUE_ONE)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> tagService.readById(LONG_VALUE_ONE));
    }

    @Test
    void readAllPositive() {
        List<Tag> tags = new ArrayList<>();
        List<TagDTO> expected = new ArrayList<>();

        when(tagDao.readAll(VALUE_PAGE, VALUE_SIZE)).thenReturn(tags);
        assertEquals(expected, tagService.readAll(VALUE_PAGE, VALUE_SIZE));
    }

    @Test
    void deleteByIdPositive() {
        assertDoesNotThrow(() -> tagService.deleteById(LONG_VALUE_ONE));
    }

    @Test
    void deleteByIdNegative() {
        doThrow(new EntityNotInDBException()).when(tagDao).deleteById(LONG_VALUE_ONE);
        assertThrows(EntityNotInDBException.class, () -> tagService.deleteById(LONG_VALUE_ONE));
    }

    @Test
    void getMostUsedTagPositive() {
        TagDTO tagDTO = new TagDTO();
        Tag tag = new Tag();

        when(userDao.getUserIdWithOrdersHighestCost()).thenReturn(LONG_VALUE_ONE);
        when(tagDao.getMostUsedUserTag(LONG_VALUE_ONE)).thenReturn(Optional.of(tag));
        when(modelMapper.map(tag, TagDTO.class)).thenReturn(tagDTO);

        assertEquals(tagDTO, tagService.getMostUsedTagFromUserWithOrdersHighestCost());
    }

    @Test
    void getMostUsedTagNegative() {
        when(userDao.getUserIdWithOrdersHighestCost()).thenReturn(LONG_VALUE_ONE);
        when(tagDao.getMostUsedUserTag(LONG_VALUE_ONE)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> tagService.getMostUsedTagFromUserWithOrdersHighestCost());
    }

}
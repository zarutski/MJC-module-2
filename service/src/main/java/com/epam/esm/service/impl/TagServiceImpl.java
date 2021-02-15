package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IdNotExistException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final UserDao userDao;
    private final ModelMapper modelMapper;

    public TagServiceImpl(TagDao tagDao, UserDao userDao, ModelMapper modelMapper) {
        this.tagDao = tagDao;
        this.userDao = userDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDTO create(TagDTO dto) {
        return tagDao.create(modelMapper.map(dto, Tag.class))
                .map(tag -> modelMapper.map(tag, TagDTO.class))
                .orElseThrow(CreateEntityInternalException::new);
    }

    @Override
    public TagDTO readById(Long id) {
        return modelMapper.map(tagDao.readById(id)
                .orElseThrow(() -> new IdNotExistException(id.toString())), TagDTO.class);
    }

    @Override
    public List<TagDTO> readAll(int page, int size) {
        return tagDao.readAll(page, size)
                .stream()
                .map(tag -> modelMapper.map(tag, TagDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        tagDao.deleteById(id);
    }

    @Override
    public Long getEntitiesCount() {
        return tagDao.getEntitiesCount();
    }

    @Override
    public TagDTO getMostUsedTagFromUserWithOrdersHighestCost() {
        Long userId = userDao.getUserIdWithOrdersHighestCost();
        Tag tag = tagDao.getMostUsedUserTag(userId).orElseThrow(() -> new IdNotExistException(userId.toString()));
        return modelMapper.map(tag, TagDTO.class);
    }
}

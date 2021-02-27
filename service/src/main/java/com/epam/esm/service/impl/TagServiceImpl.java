package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
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
    private final ModelMapper modelMapper;

    public TagServiceImpl(TagDao tagDao, ModelMapper modelMapper) {
        this.tagDao = tagDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDTO create(TagDTO tagDTO) {
        return tagDao.create(modelMapper.map(tagDTO, Tag.class))
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
        Tag tag = tagDao.readById(id).orElseThrow(() -> new IdNotExistException(id.toString()));
        tagDao.delete(tag);
    }

    @Override
    public Long getEntitiesCount() {
        return tagDao.getEntitiesCount();
    }

    @Override
    @Transactional(noRollbackFor = IdNotExistException.class)
    public TagDTO readByName(String name) {
        Tag tag = tagDao.readByName(name).orElseThrow(() -> new IdNotExistException(name));
        return modelMapper.map(tag, TagDTO.class);
    }

    @Override
    public TagDTO getMostUsedTagFromUserWithOrdersHighestCost() {
        Tag tag = tagDao.getMostUsedTagFromUserWithOrdersHighestCost()
                .orElseThrow(IdNotExistException::new);
        return modelMapper.map(tag, TagDTO.class);
    }
}

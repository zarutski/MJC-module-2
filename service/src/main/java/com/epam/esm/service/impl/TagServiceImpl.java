package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.IdNotExistException;
import com.epam.esm.service.mapper.TagDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagDTOMapper tagDTOMapper;

    public TagServiceImpl(TagDao tagDao, TagDTOMapper tagDTOMapper) {
        this.tagDao = tagDao;
        this.tagDTOMapper = tagDTOMapper;
    }

    @Override
    public List<TagDTO> readTagsByCertificateId(Long certificateId) {
        List<Tag> byCertificateId = tagDao.readTagsByCertificateId(certificateId);
        return tagDTOMapper.toTagDTOList(byCertificateId);
    }

    @Override
    @Transactional
    public Long create(TagDTO dto) {
        Optional<Tag> tagInDB = tagDao.readByTagName(dto.getName());
        if (tagInDB.isPresent()) {
            return tagInDB.get().getId();
        }

        return tagDao.createFromEntity(tagDTOMapper.toTagEntity(dto));
    }

    @Override
    public TagDTO readById(Long id) {
        return tagDTOMapper.toTagDTO(tagDao.readById(id).orElseThrow(() -> new IdNotExistException(id.toString())));
    }

    @Override
    public List<TagDTO> readAll() {
        return tagDao.readAll()
                .stream()
                .map(tagDTOMapper::toTagDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Integer deleteById(Long id) {
        return tagDao.deleteById(id);
    }

}

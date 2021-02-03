package com.epam.esm.service.mapper.impl;

import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;
import com.epam.esm.service.mapper.TagDTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagDTOMapperImpl implements TagDTOMapper {

    @Override
    public Tag toEntity(TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        return tag;
    }

    @Override
    public TagDTO toDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }

    @Override
    public List<TagDTO> toDTOList(List<Tag> tag) {
        return tag.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
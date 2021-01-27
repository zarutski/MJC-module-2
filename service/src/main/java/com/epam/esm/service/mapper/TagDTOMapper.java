package com.epam.esm.service.mapper;

import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;

import java.util.List;

public interface TagDTOMapper {

    Tag toEntity(TagDTO tagDTO);

    TagDTO toDTO(Tag tag);

    List<TagDTO> toDTOList(List<Tag> tag);

}

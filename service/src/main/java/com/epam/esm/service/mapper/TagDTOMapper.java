package com.epam.esm.service.mapper;

import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;

import java.util.List;

public interface TagDTOMapper {

    Tag toTagEntity(TagDTO tagDTO);

    TagDTO toTagDTO(Tag tag);

    List<TagDTO> toTagDTOList(List<Tag> tag);

    List<Tag> toTagEntityList(List<TagDTO> tag);
}

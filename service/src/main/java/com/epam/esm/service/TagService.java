package com.epam.esm.service;

import com.epam.esm.domain.dto.TagDTO;
import com.epam.esm.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService extends CRDService<TagDTO> {

    List<TagDTO> readByCertificateId(Long certificateId);

    Optional<Tag> readByName(String name);
}
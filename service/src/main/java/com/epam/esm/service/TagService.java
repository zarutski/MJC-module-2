package com.epam.esm.service;

import com.epam.esm.domain.dto.TagDTO;

import java.util.List;

public interface TagService extends CRDService<TagDTO> {

    List<TagDTO> readTagsByCertificateId(Long certificateId);
}
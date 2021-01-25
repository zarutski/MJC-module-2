package com.epam.esm.dao;

import com.epam.esm.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends DBOperationCRD<Tag> {

    Optional<Tag> readByTagName(String name);

    List<Tag> readTagsByCertificateId(Long certificateId);

}
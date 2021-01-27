package com.epam.esm.dao;

import com.epam.esm.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends DBOperationCRD<Tag> {

    Optional<Tag> readByName(String name);

    List<Tag> readByCertificateId(Long certificateId);

}
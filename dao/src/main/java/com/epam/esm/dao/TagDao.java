package com.epam.esm.dao;

import com.epam.esm.domain.entity.Tag;

import java.util.Optional;

public interface TagDao extends DBOperationCD<Tag>, CommonOperation<Tag> {

    Optional<Tag> readByName(String name);

    Optional<Tag> getMostUsedUserTag(Long userID);

}
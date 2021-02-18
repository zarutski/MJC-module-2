package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface CommonOperation<T> {

    Optional<T> readById(Long id);

    List<T> readAll(int page, int size);

    Long getEntitiesCount();

}
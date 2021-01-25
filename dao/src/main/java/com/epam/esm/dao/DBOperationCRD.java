package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface DBOperationCRD<T> {

    Long createFromEntity(T entity);

    Optional<T> readById(Long id);

    List<T> readAll();

    Integer deleteById(Long id);
}

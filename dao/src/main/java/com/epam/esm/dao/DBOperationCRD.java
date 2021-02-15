package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface DBOperationCRD<T> {

    Optional<T> create(T entity);

    Optional<T> readById(Long id);

    List<T> readAll(int page, int size);

    void deleteById(Long id);

}
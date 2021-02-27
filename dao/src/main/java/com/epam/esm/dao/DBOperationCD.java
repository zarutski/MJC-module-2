package com.epam.esm.dao;

import java.util.Optional;

public interface DBOperationCD<T> {

    Optional<T> create(T entity);

    void delete(T entity);

}
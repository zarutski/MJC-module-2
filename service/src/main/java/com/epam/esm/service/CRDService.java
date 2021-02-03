package com.epam.esm.service;

import java.util.List;

public interface CRDService<T> {

    Long create(T dto);

    T readById(Long id);

    List<T> readAll();

    Integer deleteById(Long id);
}

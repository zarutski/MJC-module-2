package com.epam.esm.service;

import java.util.List;

public interface CRDService<T> {

    T create(T dto);

    T readById(Long id);

    List<T> readAll(int page, int size);

    void deleteById(Long id);

    Long getEntitiesCount();
}
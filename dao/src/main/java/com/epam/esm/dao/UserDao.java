package com.epam.esm.dao;

import com.epam.esm.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> readById(Long id);

    List<User> readAll(int page, int size);

    Long getEntitiesCount();

    Long getUserIdWithOrdersHighestCost();
}

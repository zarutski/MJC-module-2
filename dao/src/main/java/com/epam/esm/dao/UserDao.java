package com.epam.esm.dao;

import com.epam.esm.domain.entity.User;

import java.util.Optional;

public interface UserDao extends CommonOperation<User> {

    Optional<User> findByLogin(String login);

    Optional<User> create(User user);
}
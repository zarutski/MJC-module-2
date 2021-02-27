package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.domain.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserDaoImpl extends CommonOperationDao<User> implements UserDao {

    public UserDaoImpl(EntityManager entityManager) {
        super(entityManager, User.class);
        this.entityManager = entityManager;
    }
}
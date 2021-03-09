package com.epam.esm.dao.impl;

import com.epam.esm.dao.RoleDao;
import com.epam.esm.domain.entity.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class RoleDaoImpl extends CommonOperationDao<Role> implements RoleDao {

    public RoleDaoImpl(EntityManager entityManager) {
        super(entityManager, Role.class);
    }
}
package com.epam.esm.dao;

import com.epam.esm.domain.entity.User;

public interface UserDao extends CommonOperation<User> {

    Long getUserIdWithOrdersHighestCost();
}

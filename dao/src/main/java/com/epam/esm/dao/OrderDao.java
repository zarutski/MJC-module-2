package com.epam.esm.dao;

import com.epam.esm.domain.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    List<Order> readAll(int page, int size);

    Optional<Order> readById(Long id);

    List<Order> readOrdersByUserId(Long userId, int page, int size);

    Optional<Order> create(Order order);

    Long getUserOrderCount(Long userId);

    Long getEntitiesCount();
}
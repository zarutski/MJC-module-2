package com.epam.esm.dao;

import com.epam.esm.domain.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends CommonOperation<Order> {

    List<Order> readOrdersByUserId(Long userId, int page, int size);

    Optional<Order> create(Order order);

    Long getUserOrderCount(Long userId);

    Optional<Order> readUserOrder(Long userId, Long orderId);

}
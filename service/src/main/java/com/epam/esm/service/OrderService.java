package com.epam.esm.service;

import com.epam.esm.domain.dto.CreateOrderDTO;
import com.epam.esm.domain.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> readAll(int page, int size);

    OrderDTO readById(Long id);

    List<OrderDTO> readOrdersByUserId(Long userID, int page, int size);

    OrderDTO create(CreateOrderDTO createOrderDTO);

    Long getUserOrderCount(Long userId);

    Long getEntitiesCount();

    OrderDTO readUserOrder(Long userId, Long orderId);

}
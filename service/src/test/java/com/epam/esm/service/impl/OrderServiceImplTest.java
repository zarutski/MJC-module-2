package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.dao.exception.EntityNotInDBException;
import com.epam.esm.domain.dto.CreateOrderDTO;
import com.epam.esm.domain.dto.OrderDTO;
import com.epam.esm.domain.entity.Order;
import com.epam.esm.service.exception.IdNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private OrderServiceImpl orderService;

    private static final Integer VALUE_PAGE = 1;
    private static final Integer VALUE_SIZE = 3;
    private static final Long LONG_VALUE_ONE = 1L;

    @Test
    void readAllPositive() {
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();
        List<OrderDTO> orderDtoList = Stream.of(orderDTO).collect(Collectors.toList());
        List<Order> orderList = Stream.of(order).collect(Collectors.toList());

        when(orderDao.readAll(VALUE_PAGE, VALUE_SIZE)).thenReturn(orderList);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        assertEquals(orderDtoList, orderService.readAll(VALUE_PAGE, VALUE_SIZE));
    }

    @Test
    void readByIdPositive() {
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();

        when(orderDao.readById(LONG_VALUE_ONE)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        assertEquals(orderDTO, orderService.readById(LONG_VALUE_ONE));
    }

    @Test
    void readByIdNegative() {
        when(orderDao.readById(LONG_VALUE_ONE)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> orderService.readById(LONG_VALUE_ONE));
    }

    @Test
    void readOrdersByUserIdPositive() {
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();
        List<OrderDTO> orderDtoList = Stream.of(orderDTO).collect(Collectors.toList());
        List<Order> orderList = Stream.of(order).collect(Collectors.toList());

        when(orderDao.readOrdersByUserId(LONG_VALUE_ONE, VALUE_PAGE, VALUE_SIZE)).thenReturn(orderList);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        assertEquals(orderDtoList, orderService.readOrdersByUserId(LONG_VALUE_ONE, VALUE_PAGE, VALUE_SIZE));
    }

    @Test
    void readOrdersByUserIdNegative() {
        when(orderDao.readOrdersByUserId(LONG_VALUE_ONE, VALUE_PAGE, VALUE_SIZE)).thenThrow(new EntityNotInDBException());
        assertThrows(EntityNotInDBException.class, () -> orderService.readOrdersByUserId(LONG_VALUE_ONE, VALUE_PAGE, VALUE_SIZE));
    }

    @Test
    void createPositive() {
        OrderDTO orderDTO = new OrderDTO();
        Order order = new Order();
        order.setCertificateList(new ArrayList<>());
        order.setCost(BigDecimal.ZERO);
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setCertificateIds(new ArrayList<>());

        when(orderDao.create(order)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        assertEquals(orderDTO, orderService.create(createOrderDTO));
    }

    @Test
    void createNegative() {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setCertificateIds(new ArrayList<>());
        assertThrows(CreateEntityInternalException.class, () -> orderService.create(createOrderDTO));
    }


    @Test
    void getUserOrderCount() {
        when(orderDao.getUserOrderCount(LONG_VALUE_ONE)).thenReturn(LONG_VALUE_ONE);
        assertEquals(LONG_VALUE_ONE, orderService.getUserOrderCount(LONG_VALUE_ONE));
    }
}
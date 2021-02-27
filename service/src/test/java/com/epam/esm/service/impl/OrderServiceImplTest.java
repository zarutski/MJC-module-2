package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.CreateEntityInternalException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderServiceImpl orderService;

    private static final Integer PAGE_NUMBER = 1;
    private static final Integer RECORDS_PER_PAGE = 3;
    private static final Long RECORD_ID = 1L;
    private static final Long ENTITIES_COUNT = 2L;

    @Test
    void readAllPositive() {
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();
        List<OrderDTO> orderDtoList = Stream.of(orderDTO).collect(Collectors.toList());
        List<Order> orderList = Stream.of(order).collect(Collectors.toList());

        when(orderDao.readAll(PAGE_NUMBER, RECORDS_PER_PAGE)).thenReturn(orderList);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        assertEquals(orderDtoList, orderService.readAll(PAGE_NUMBER, RECORDS_PER_PAGE));
    }

    @Test
    void readByIdPositive() {
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();

        when(orderDao.readById(RECORD_ID)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        assertEquals(orderDTO, orderService.readById(RECORD_ID));
    }

    @Test
    void readByIdNegative() {
        when(orderDao.readById(RECORD_ID)).thenReturn(Optional.empty());
        assertThrows(IdNotExistException.class, () -> orderService.readById(RECORD_ID));
    }

    @Test
    void readOrdersByUserIdPositive() {
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();
        List<OrderDTO> orderDtoList = Stream.of(orderDTO).collect(Collectors.toList());
        List<Order> orderList = Stream.of(order).collect(Collectors.toList());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(RECORD_ID);

        when(orderDao.readOrdersByUserId(RECORD_ID, PAGE_NUMBER, RECORDS_PER_PAGE)).thenReturn(orderList);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        when(userService.readById(RECORD_ID)).thenReturn(userDTO);
        assertEquals(orderDtoList, orderService.readOrdersByUserId(RECORD_ID, PAGE_NUMBER, RECORDS_PER_PAGE));
    }

    @Test
    void readOrdersByUserIdNegative() {
        when(userService.readById(RECORD_ID)).thenThrow(new IdNotExistException());
        assertThrows(IdNotExistException.class, () -> orderService.readOrdersByUserId(RECORD_ID, PAGE_NUMBER, RECORDS_PER_PAGE));
    }

    @Test
    void createPositive() {
        OrderDTO orderDTO = new OrderDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(RECORD_ID);
        Order order = new Order();
        order.setUserId(RECORD_ID);
        order.setCertificates(new ArrayList<>());
        order.setCost(BigDecimal.ZERO);
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setUserId(RECORD_ID);
        createOrderDTO.setCertificateIds(new ArrayList<>());

        when(userService.readById(RECORD_ID)).thenReturn(userDTO);
        when(orderDao.create(order)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);
        assertEquals(orderDTO, orderService.create(createOrderDTO));
    }

    @Test
    void createNegative() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(RECORD_ID);
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setCertificateIds(new ArrayList<>());
        createOrderDTO.setUserId(RECORD_ID);

        when(userService.readById(RECORD_ID)).thenReturn(userDTO);
        assertThrows(CreateEntityInternalException.class, () -> orderService.create(createOrderDTO));
    }


    @Test
    void getUserOrderCount() {
        when(orderDao.getUserOrderCount(RECORD_ID)).thenReturn(ENTITIES_COUNT);
        assertEquals(ENTITIES_COUNT, orderService.getUserOrderCount(RECORD_ID));
    }
}
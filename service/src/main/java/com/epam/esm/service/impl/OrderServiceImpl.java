package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.domain.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.domain.dto.CreateOrderDTO;
import com.epam.esm.domain.dto.OrderDTO;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Order;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.IdNotExistException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ModelMapper modelMapper;
    private final CertificateService certificateService;
    private final UserService userService;

    public OrderServiceImpl(OrderDao orderDao, ModelMapper modelMapper, CertificateService certificateService,
                            UserService userService) {
        this.orderDao = orderDao;
        this.modelMapper = modelMapper;
        this.certificateService = certificateService;
        this.userService = userService;
    }

    @Override
    public List<OrderDTO> readAll(int page, int size) {
        return orderDao.readAll(page, size).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO readById(Long id) {
        Order order = orderDao.readById(id).orElseThrow(() -> new IdNotExistException(id.toString()));
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> readOrdersByUserId(Long userId, int page, int size) {
        userId = getExistingUserId(userId);
        return orderDao.readOrdersByUserId(userId, page, size)
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO create(CreateOrderDTO createOrderDTO) {
        Long userId = getExistingUserId(createOrderDTO.getUserId());
        List<Certificate> certificates = getCertificateList(createOrderDTO);
        Order order = buildOrder(userId, certificates);
        return modelMapper.map(
                orderDao.create(order).orElseThrow(CreateEntityInternalException::new),
                OrderDTO.class);
    }

    @Override
    public Long getUserOrderCount(Long userId) {
        return orderDao.getUserOrderCount(userId);
    }

    @Override
    public Long getEntitiesCount() {
        return orderDao.getEntitiesCount();
    }

    private Long getExistingUserId(Long userId) {
        UserDTO existing = userService.readById(userId);
        return existing.getId();
    }

    private List<Certificate> getCertificateList(CreateOrderDTO createOrderDTO) {
        return createOrderDTO.getCertificateIds()
                .stream()
                .map(giftId -> modelMapper.map(certificateService.readById(giftId), Certificate.class))
                .collect(Collectors.toList());
    }

    private Order buildOrder(Long userId, List<Certificate> certificates) {
        Order order = new Order();
        order.setUserId(userId);
        BigDecimal cost = certificates.stream().map(Certificate::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setCost(cost);
        order.setCertificateList(certificates);
        return order;
    }
}

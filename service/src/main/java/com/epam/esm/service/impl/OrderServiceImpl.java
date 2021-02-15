package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.service.exception.CreateEntityInternalException;
import com.epam.esm.domain.dto.CreateOrderDTO;
import com.epam.esm.domain.dto.OrderDTO;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Order;
import com.epam.esm.dao.impl.CertificateDaoImpl;
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

    OrderDao orderDao;
    ModelMapper modelMapper;
    CertificateService certificateService;
    CertificateDao certificateDao;

    public OrderServiceImpl(OrderDao orderDao, ModelMapper modelMapper, CertificateService certificateService,
                            CertificateDao certificateDao) {
        this.orderDao = orderDao;
        this.modelMapper = modelMapper;
        this.certificateService = certificateService;
        this.certificateDao = certificateDao;
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
        return orderDao.readOrdersByUserId(userId, page, size)
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO create(CreateOrderDTO createOrderDTO) {
        // get create parameters
        List<Long> certificateIds = createOrderDTO.getCertificateIds();
        Long userId = createOrderDTO.getUserId();

        // get certificate entities
        List<Certificate> certificates = certificateIds.stream()
                .map(giftId ->
                        certificateDao.readById(giftId).orElseThrow((() -> new IdNotExistException(giftId.toString())))
                ).collect(Collectors.toList());

        // create order entity object
        Order order = new Order();
        order.setUserId(userId);
        BigDecimal cost = certificates.stream().map(Certificate::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setCost(cost);
        order.setCertificateList(certificates);

        // save order
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
}

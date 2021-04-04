package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.domain.entity.Certificate;
import com.epam.esm.domain.entity.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class OrderDaoImplTest {

    private static final Long RECORD_ID = 1L;
    private static final int YEAR = 2021;
    private static final int MONTH = 2;
    private static final int DAY = 8;
    private static final int HOUR = 22;
    private static final int MINUTE = 12;
    private static final int SECOND = 48;
    private static final BigDecimal COST = BigDecimal.valueOf(200);

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private CertificateDao certificateDao;

    @Test
    void readOrdersByUserId(){
        Order expected = createDBIdentityOrder();
        Optional<Order> order = orderDao.readById(RECORD_ID);
        assertEquals(expected, order.orElseThrow(RuntimeException::new));
    }

    @Test
    void create(){
        Order expected = createOrder();
        Optional<Order> order = orderDao.create(expected);
        assertEquals(expected, order.orElseThrow(RuntimeException::new));
    }

    @Test
    void readUserOrder() {
        Order expected = createDBIdentityOrder();
        Optional<Order> order = orderDao.readUserOrder(RECORD_ID, RECORD_ID);
        assertEquals(expected, order.orElseThrow(RuntimeException::new));
    }

    private Order createDBIdentityOrder() {
        Order order = new Order();
        order.setId(RECORD_ID);
        order.setCost(COST);
        LocalDateTime date = LocalDateTime.of(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND);
        order.setDate(date);
        order.setUserId(RECORD_ID);
        List<Certificate> certificates = Collections.singletonList(getCertificate());
        order.setCertificates(certificates);
        return order;
    }

    private Order createOrder() {
        Order order = new Order();
        order.setCost(COST);
        order.setUserId(RECORD_ID);
        List<Certificate> certificates = Collections.emptyList();
        order.setCertificates(certificates);
        return order;
    }

    private Certificate getCertificate() {
        return certificateDao.readById(RECORD_ID).orElseThrow(RuntimeException::new);
    }
}
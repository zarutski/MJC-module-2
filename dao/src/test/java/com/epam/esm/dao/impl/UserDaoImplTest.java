package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.RoleDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.config.TestConfig;
import com.epam.esm.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class UserDaoImplTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RoleDao roleDao;

    private static final Long FIRST_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Integer PAGE_NUMBER = 1;
    private static final Integer RECORDS_PER_PAGE = 2;

    private static final String FIRST_NAME = "Jessamine";
    private static final String LAST_NAME = "Radin";
    private static final String USER_EMAIL = "jradin8@senate.gov";
    private static final String USER_LOGIN = "jradin8";
    private static final String PASSWORD = "$2a$12$d5BiJASvcPFSDUfJMtLjW.wsb6on93mi/3Zx0wyUH1FFiB2UU/Fa.";
    private static final String LOGIN = "login@senate.gov";
    private static final String EMAIL = "login";


    @Test
    void findByLogin(){
        User expected = createDBIdentityUser();
        Optional<User> user = userDao.findByLogin(USER_LOGIN);
        assertEquals(expected, user.orElseThrow(RuntimeException::new));
    }

    @Test
    void create() {
        User expected = createUser();
        Optional<User> actual = userDao.create(expected);
        assertEquals(expected, actual.orElseThrow(RuntimeException::new));
    }

    private User createDBIdentityUser() {
        User user = new User();
        user.setId(FIRST_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(USER_EMAIL);
        user.setLogin(USER_LOGIN);
        user.setPassword(PASSWORD);
        user.setOrderList(orderDao.readOrdersByUserId(FIRST_ID, PAGE_NUMBER, RECORDS_PER_PAGE));
        user.setRole(roleDao.readById(SECOND_ID).orElseThrow(RuntimeException::new));
        return user;
    }

    private User createUser() {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        user.setLogin(LOGIN);
        user.setPassword(PASSWORD);
        user.setRole(roleDao.readById(SECOND_ID).orElseThrow(RuntimeException::new));
        return user;
    }
}